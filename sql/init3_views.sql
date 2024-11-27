USE Grocery;

USE Grocery;

CREATE VIEW v_item AS
SELECT
    i.id,
    i.name,
    i.currentPrice,
    i.supplier,
    i.unitType,
    i.discount,
    ISNULL(r.total_restocked, 0) AS totalRestocked,
    ISNULL(s.total_sold, 0) AS totalSold,
    (ISNULL(r.total_restocked, 0) - ISNULL(s.total_sold, 0)) AS derivedCurrentStock,
    i.cachedCurrentStock,
    i.targetAmount
FROM
	item i
LEFT JOIN (
    SELECT
        ri.itemID,
        SUM(ri.quantity) AS total_restocked
    FROM restockItem AS ri
    JOIN
        restock AS r ON ri.restockID = r.id
    WHERE
        r.status = 'restocked'
    GROUP BY ri.itemID
) AS r ON i.id = r.itemID
LEFT JOIN (
    SELECT
        si.itemID,
        SUM(si.quantity) AS total_sold
    FROM saleItem AS si
    GROUP BY si.itemID
) AS s ON i.id = s.itemID;

CREATE VIEW v_sale AS
SELECT
    s.id,
    s.employeeID,
    s.timestamp,
    s.tip,
    s.paymentID,
    COALESCE(SUM(si.totalCost), 0) AS total,
    COALESCE(SUM(si.totalCost), 0) * s.tip AS tipAmount
FROM
    sale AS s
LEFT JOIN
    saleItem AS si ON s.id = si.saleID
GROUP BY
    s.id, s.employeeID, s.timestamp, s.tip, s.paymentID;

CREATE VIEW v_restock AS
SELECT
    r.id,
    r.supplierID,
    r.status,
    r.orderDate,
    r.confirmDate,
    r.deliveryDate,
    r.restockDate,
    r.additionalCost,
    COALESCE(SUM(ri.totalCost), 0) + r.additionalCost AS totalCost
FROM
    restock r
LEFT JOIN
    restockItem ri ON r.id = ri.restockID
GROUP BY
    r.id,
    r.supplierID,
    r.status,
    r.orderDate,
    r.confirmDate,
    r.deliveryDate,
    r.restockDate,
    r.additionalCost;
