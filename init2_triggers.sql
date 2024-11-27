USE Grocery;

GO;

CREATE TRIGGER trg_increase_stock
ON restock
AFTER UPDATE
AS
BEGIN
    IF UPDATE(status)
    BEGIN
        UPDATE item
        SET cachedCurrentStock = cachedCurrentStock + ri.quantity
        FROM item
        JOIN restockItem AS ri ON item.id = ri.itemID
        JOIN inserted AS i ON ri.restockID = i.id
        JOIN deleted AS d ON i.id = d.id
        WHERE d.status <> i.status
        AND i.status = 'restocked';
    END
END;

GO;

CREATE TRIGGER trg_decrease_stock
ON saleItem
AFTER INSERT
AS
BEGIN
    UPDATE item
    SET cachedCurrentStock = cachedCurrentStock - si.quantity
    FROM item
    JOIN saleItem AS si ON item.id = si.itemID
    JOIN inserted AS i ON si.saleID = i.saleID AND si.itemID = i.itemID;
END;

GO;

CREATE TRIGGER trg_CheckStockAndRestock
ON item
AFTER UPDATE
AS
BEGIN
    DECLARE @ItemID INT;
    DECLARE @Supplier INT;
    DECLARE @CurrentPrice INT;
    DECLARE @TargetAmount INT;

    DECLARE stock_cursor CURSOR FOR
    SELECT id, supplier, currentPrice, targetAmount
    FROM inserted
    WHERE cachedCurrentStock < targetAmount;

    OPEN stock_cursor;

    FETCH NEXT FROM stock_cursor INTO @ItemID, @Supplier, @CurrentPrice, @TargetAmount;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        DECLARE @RestockID INT;
        SELECT @RestockID = id
        FROM restock AS r
        WHERE r.supplierID = @Supplier
            AND r.status = 'created';

        IF @RestockID IS NULL
        BEGIN
            INSERT INTO restock (supplierID, status, additionalCost)
            VALUES (@Supplier, 'created', 10);

            SET @RestockID = SCOPE_IDENTITY();
        END

        DECLARE @RestockItemID INT;
        SELECT @RestockItemID = id
        FROM restockItem AS ri
        JOIN restock AS r ON r.id = ri.restockID
        WHERE ri.itemID = @ItemID AND r.status = 'created' AND r.supplierID = @Supplier;

        IF @RestockItemID IS NULL
        BEGIN
            INSERT INTO restockItem (restockID, itemID, quantity, expiryDate, unitCost)
            VALUES (@RestockID, @ItemID, @TargetAmount, DATEADD(month, 2, GETDATE()), @CurrentPrice);
        END

        FETCH NEXT FROM stock_cursor INTO @ItemID, @Supplier, @CurrentPrice, @TargetAmount;
    END

    CLOSE stock_cursor;
    DEALLOCATE stock_cursor;
END;

