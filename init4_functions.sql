USE Grocery;

DROP TRIGGER add_manufacturer;

DROP PROCEDURE InsertBarcode;
CREATE PROCEDURE InsertBarcode
    @ean13 VARCHAR(13),
    @itemID INT
AS
BEGIN
    DECLARE @GSPrefix VARCHAR(3),
            @ManufacturerCode VARCHAR(4),
            @ProductCode VARCHAR(5),
            @CheckDigit CHAR(1);

    IF LEN(@ean13) = 13 AND @ean13 NOT LIKE '%[^0-9]%'
    BEGIN
        SET @GSPrefix = SUBSTRING(@ean13, 1, 3);
        SET @ManufacturerCode = SUBSTRING(@ean13, 4, 4);
        SET @ProductCode = SUBSTRING(@ean13, 8, 5);
        SET @CheckDigit = SUBSTRING(@ean13, 13, 1);

        DECLARE @CalculatedCheckDigit INT;
        DECLARE @Sum INT = 0;
        DECLARE @Index INT = 1;

        WHILE @Index <= 12
        BEGIN
            SET @Sum = @Sum + CAST(SUBSTRING(@ean13, @Index, 1) AS INT) *
                        CASE WHEN @Index % 2 = 0 THEN 3 ELSE 1 END;
            SET @Index = @Index + 1;
        END

        SET @CalculatedCheckDigit = (10 - (@Sum % 10)) % 10;

        IF CAST(@CheckDigit AS INT) = @CalculatedCheckDigit
        BEGIN
	        DECLARE @ManuExists INT;
	        SELECT @ManuExists = id
	        FROM manufacturer
	        WHERE id=@ManufacturerCode;
	       
	       	IF @ManuExists IS NULL
	       	BEGIN
	       		INSERT INTO manufacturer(id) VALUES
	       		(@ManufacturerCode);
	       	END
	       
            INSERT INTO barcode (manufacturer, gsPrefix, productCode, itemID)
            VALUES (
                @ManufacturerCode,
                @GSPrefix,
                @ProductCode,
                @itemID
            );
            PRINT 'Barcode inserted successfully.';
        END
        ELSE
        BEGIN
            PRINT 'Invalid EAN-13 barcode: Check digit does not match.';
        END
    END
    ELSE
    BEGIN
        PRINT 'Invalid EAN-13 barcode: Must be exactly 13 digits.';
    END
END;
