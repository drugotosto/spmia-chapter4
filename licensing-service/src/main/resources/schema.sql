DROP TABLE IF EXISTS licenses;

CREATE TABLE licenses (
  license_id        VARCHAR(100) PRIMARY KEY NOT NULL,
  organization_id   TEXT NOT NULL,
  license_type      TEXT NOT NULL,
  product_name      TEXT NOT NULL,
  license_max       INT  NOT NULL,
  license_allocated INT,
  comment           VARCHAR(100));


INSERT INTO licenses (license_id,  organization_id, license_type, product_name, license_max, license_allocated)
VALUES ('PROD_000001', 'COMP_000001', 'Home Edition','Windows 10', 439,366);
INSERT INTO licenses (license_id,  organization_id, license_type, product_name, license_max, license_allocated)
VALUES ('PROD_000002', 'COMP_000001', 'Professional Edition','Office 2016', 200,189);
INSERT INTO licenses (license_id, organization_id, license_type, product_name, license_max, license_allocated)
VALUES ('PROD_000003', 'COMP_000002', 'Standard Edition','MySQL', 100,44);
INSERT INTO licenses (license_id,  organization_id, license_type, product_name, license_max, license_allocated)
VALUES ('PROD_000004', 'COMP_000002', 'Professional Edition','Oracle ERP', 80,16);