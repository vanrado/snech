/* 9.1. 2015 */
insert into users(first_name, last_name, email, occupation, created_on) values ('Robert', 'Langdon', 'radovan.racak@gmail.com', 'Developer', to_timestamp('31-12-2006 23:34:59','DD-MM-YYYY   HH24:MI:SS'));
delete from users where user_id=2;

/* Next we create the package that contains the specification of the security code. *//*
CREATE OR REPLACE PACKAGE user_security AS

  FUNCTION get_hash (p_username  IN  VARCHAR2,
                     p_password  IN  VARCHAR2)
    RETURN VARCHAR2;
    
  PROCEDURE add_user (p_username  IN  VARCHAR2,
                      p_password  IN  VARCHAR2);

  PROCEDURE change_password (p_username      IN  VARCHAR2,
                             p_old_password  IN  VARCHAR2,
                             p_new_password  IN  VARCHAR2);

  PROCEDURE valid_user (p_username  IN  VARCHAR2,
                        p_password  IN  VARCHAR2);

  FUNCTION valid_user (p_username  IN  VARCHAR2,
                       p_password  IN  VARCHAR2)
    RETURN BOOLEAN;

END;
/
*/

insert into user_roles(ROLE_NAME) values('user');
insert into user_roles(ROLE_NAME) values('developer');
insert into user_roles(ROLE_NAME) values('admin');
