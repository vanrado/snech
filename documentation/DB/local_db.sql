alter table issues 
modify(progress number(1));

update issues
set progress=0
where progress is null;

/* Selektuje hodnotu sequence */
select last_number
from user_sequences
where sequence_name='UPLOAD_FOLDER_SEQ';

select login, user_id
inner join users on user_id=users.user_id 
from user_logins where role_id=2;

SELECT * FROM user_logins inner join users on user_logins.user_id = users.user_id inner join user_roles on user_logins.role_id=user_roles.ROLE_ID where login='robert_u';

delete from attachments where attachment_id=12;

update issues set progress=70 where issue_id=121;
select * from issues where issue_id=267;
SELECT * FROM issues order by created_on DESC;


select users.FIRST_NAME, users.LAST_NAME, user_roles.role_name, login from user_logins
inner join user_roles on user_roles.ROLE_ID = user_logins.ROLE_ID
inner join users on users.USER_ID = user_logins.USER_ID
where role_name='technik';



/* Zabezpecenie ze uzivatel bude mat len jeden ucet uzivatela, technika a admina */
/**/
create or replace trigger user_login_trg
before
insert on user_logins
for each row
declare 
  v_pocetRoli number;
begin
  select count(*)
  into v_pocetRoli
  from user_logins
  where user_id=:new.user_id and role_id=:new.role_id;
  
  if(v_pocetRoli = 0) 
  then
    dbms_output.put_line('Mozme vlozit'); 
  else
    raise_application_error(-20001, 'Uzivatel s id #'||:new.user_id||' uz ma pridelenu rolu s id #'||:new.role_id);
  end if;
end;
/

select count(*)
from user_logins
where user_id=1 and role_id=2;

select role_id from user_logins where user_id=1;

select upload_folder_seq.NEXTVAL from dual;

insert into attachments(attachment_id, ISSUE_ID, message_id, file_url, file_name, file_size) values (ATTACHMENT_ID_seq.nextval, 199, null, 'url', 'nazov', 5000045612);
select * from attachments
where issue_id=199 and message_id is null;

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
VALUES (2, 'Oznam druhy', CURRENT_TIMESTAMP, '1', 1, 'Tak toto je sprava a oznaam, budeme vypinat servery v stredu 16.5.2015');

select * from notices
where visible = '1';

update issues
set subject='Ccxzcxz', code_priority='A', message=''
where issue_id=125;

update users
set first_name='Loles', last_name='Boles', email='odveci@email.sk', occupation='udrzbar';

update user_logins
set password='1234'
where login='robert_a';

UPDATE Customers
SET ContactName='Alfred Schmidt', City='Hamburg'
WHERE CustomerName='Alfreds Futterkiste';

update user_logins set password='ec5f86977d0dc3544f62106b29947b336227d912e41aaa5c09219e7273235c8f', salt='563cc1dd8a38141756c9bc95c1f8463d' where login='robert_u';

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
