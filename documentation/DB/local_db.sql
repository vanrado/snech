/* 9.1. 2015 */
insert into users(first_name, last_name, email, occupation, created_on) values ('Robert', 'Langdon', 'radovan.racak@gmail.com', 'Developer', to_timestamp('31-12-2006 23:34:59','DD-MM-YYYY   HH24:MI:SS'));
delete from users where user_id=2;
SELECT * FROM ISSUES where issue_id=1;

/* 11.3. 2015 */
Insert into SNECH_DB.ISSUES (ISSUE_ID,USER_LOGIN,SUBJECT,CODE_PRIORITY,CODE_STATUS,ESTIMATED_TIME,CREATED_ON,LAST_UPDATE,MESSAGE,ADMIN_LOGIN) values ('1','robert_u','NullPointerException SR1501','A','NOVA',to_timestamp('10.09.02 14:10:10,123000000','DD.MM.RR HH24:MI:SSXFF'),to_timestamp('10.09.02 14:10:10,123000000','DD.MM.RR HH24:MI:SSXFF'),to_timestamp('10.09.02 14:10:10,123000000','DD.MM.RR HH24:MI:SSXFF'),'Tak toto je sprava','1');
Insert into SNECH_DB.ISSUES (ISSUE_ID,USER_LOGIN,SUBJECT,CODE_PRIORITY,CODE_STATUS,ESTIMATED_TIME,CREATED_ON,LAST_UPDATE,MESSAGE,ADMIN_LOGIN) values ('2','robert_u','SqlException SR1501','A','NOVA',to_timestamp('10.09.02 14:10:10,123000000','DD.MM.RR HH24:MI:SSXFF'),to_timestamp('10.09.02 14:10:10,123000000','DD.MM.RR HH24:MI:SSXFF'),to_timestamp('10.09.02 14:10:10,123000000','DD.MM.RR HH24:MI:SSXFF'),'Tak toto je sprava','1');

select distinct * from issue_logs, issues
where issues.USER_LOGIN = 'robert_u';

SELECT * FROM issues where user_login='robert_u';

select * from user_logins 
inner join users on user_logins.user_id = users.user_id;

SELECT * FROM user_logins
inner join users on user_logins.user_id = users.user_id 
where login=? and password=?;

select * 
from ISSUE_LOGS 
where issue_id in (select issue_id from issues where user_login='robert_u')
order by created_on DESC;

INSERT INTO "SNECH_DB"."NOTICES" (NOTICE_ID, SUBJECT, CREATED_ON, VISIBLE, AUTHOR_ID, MESSAGE) 
VALUES (1, 'Oznam prvy', TO_TIMESTAMP('2015-03-22 11:53:46.948000000', 'YYYY-MM-DD HH24:MI:SS.FF'), '1', 1, 'Tak toto je sprava a oznaam, budeme vypinat servery v stredu 16.5.2015');

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
