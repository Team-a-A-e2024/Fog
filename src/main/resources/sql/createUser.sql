DO $$
DECLARE
email_address varchar := 'test@test.dk';
   	password varchar := 'test';
	role varchar := 'salesman';
BEGIN
INSERT INTO users (email, password, role)
VALUES (email_address, password, role);
END $$;