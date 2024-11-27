USE Grocery;

CREATE INDEX idx_barcode
ON barcode (barcode);

CREATE INDEX idx_employee
ON employee (fName);

CREATE INDEX idx_sale
ON sale (employeeID, paymentID);

CREATE INDEX idx_shift
ON shift (employeeID, paymentID);

CREATE INDEX idx_item
ON item (name);
