delete from service where code in ('LAMS', 'AGREEMENT', 'LAMS-REPORTS');

insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId) VALUES (nextval('SEQ_SERVICE'),'LAMS', 'Leases And Agreements', true, '/lams-web', 'Leases & Agreements', 21, NULL, 'default');
insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId) VALUES (NEXTVAL('SEQ_SERVICE'),'AGREEMENT','Agreement',true,'/lams-web', 'Agreement', 1, (select id from service where code = 'LAMS'), 'default');
insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (NEXTVAL('SEQ_SERVICE'),'LAMS-REPORTS','LAMS-Reports',true,'/lams-web', 'LAMS-Reports', 2, (select id from service where code = 'LAMS'), 'default');
insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId) VALUES (nextval('SEQ_SERVICE'),'LAMS-SERVICES', 'LAMS Services', false, '/lams-services', 'LAMS Services', 21, NULL, 'default');

--LAMS
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Create Agreement','/app/search-assets/search-asset.html','AGREEMENT','tenantId=',(select id from service where code='AGREEMENT'), 1,'Create Agreement',true,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Search Agreement','/app/search-agreement/search-agreement.html','AGREEMENT','tenantId=',(select id from service where code='AGREEMENT'), 1,'Search Agreement',true,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Search Agreement Doc types','/document/_search','LAMS-SERVICES','tenantId=',(select id from service where code='LAMS-SERVICES'), 1,'Search Agreement Doc types',false,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Search LAMS Configurations','/lamsconfigurations/_search','LAMS-SERVICES','tenantId=',(select id from service where code='LAMS-SERVICES'), 1,'Search LAMS Configurations',false,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Get Agreement Status','/getstatus','LAMS-SERVICES','tenantId=',(select id from service where code='LAMS-SERVICES'), 1,'Get Agreement Status',false,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Get Agreement Payment Cycle','/getpaymentcycle','LAMS-SERVICES','tenantId=',(select id from service where code='LAMS-SERVICES'), 1,'Get Agreement Payment Cycle',false,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Get Agreement Natureofallottment','/getnatureofallotment','LAMS-SERVICES','tenantId=',(select id from service where code='LAMS-SERVICES'), 1,'Get Agreement Natureofallottment',false,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Get Agreement rentincrements','/getrentincrements','LAMS-SERVICES','tenantId=',(select id from service where code='LAMS-SERVICES'), 1,'Get Agreement rentincrements',false,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Agreement Payment Create','/payment/_create','LAMS-SERVICES','tenantId=',(select id from service where code='LAMS-SERVICES'), 1,'Agreement Payment Create',false,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Agreement Coll back update','/payment/_update','LAMS-SERVICES','tenantId=',(select id from service where code='LAMS-SERVICES'), 1,'Agreement Coll back update',false,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Update Agreement','/agreements/_update','LAMS-SERVICES','tenantId=',(select id from service where code='LAMS-SERVICES'), 1,'Update Agreement',false,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Create Agreement Submit','/agreements/_create','LAMS-SERVICES','tenantId=',(select id from service where code='LAMS-SERVICES'), 1,'Create Agreement Submit',false,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Search Agreement Submit','/agreements/_search','LAMS-SERVICES','tenantId=',(select id from service where code='LAMS-SERVICES'), 1,'Search Agreement Submit',false,1,now(),1,now(),'default');
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Inbox Agreement View','/app/search-agreement/view-renew-agreement.html','AGREEMENT','tenantId=',(select id from service where code='AGREEMENT'), 1,'Inbox Agreement View',false,1,now(),1,now(),'default');

--boundary
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) values (nextval('SEQ_EG_ACTION'), 'Get Boundaries by boundarytype and hierarchy Type','/getnatureofallotment','BNDRY','tenantId=',(select id from service where code='BNDRY'), 1,'Get Boundaries by boundarytype and hierarchy Type',false,1,now(),1,now(),'default');

--LAMS Super User Mapping
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Create Agreement'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Search Agreement'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Search Agreement Doc types'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Search LAMS Configurations'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Get Agreement Status'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Get Agreement Payment Cycle'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Get Agreement Natureofallottment'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Get Agreement rentincrements'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Agreement Payment Create'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Agreement Coll back update'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Update Agreement'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Create Agreement Submit'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Search Agreement Submit'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Inbox Agreement View'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Create Agreement'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Search Agreement'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Verifier', (select id from eg_action where name = 'Search Agreement'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Approver', (select id from eg_action where name = 'Search Agreement'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Search Agreement Doc types'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Search LAMS Configurations'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Get Agreement Status'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Get Agreement Payment Cycle'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Get Agreement Natureofallottment'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Get Agreement rentincrements'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Agreement Payment Create'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Agreement Coll back update'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Update Agreement'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Verifier', (select id from eg_action where name = 'Update Agreement'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Approver', (select id from eg_action where name = 'Update Agreement'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Create Agreement Submit'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Search Agreement Submit'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Verifier', (select id from eg_action where name = 'Search Agreement Submit'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Approver', (select id from eg_action where name = 'Search Agreement Submit'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Inbox Agreement View'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Verifier', (select id from eg_action where name = 'Inbox Agreement View'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Approver', (select id from eg_action where name = 'Inbox Agreement View'),'default');

--boundary mapping
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Get Boundaries by boundarytype and hierarchy Type'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'Get Boundaries by boundarytype and hierarchy Type'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Verifier', (select id from eg_action where name = 'Get Boundaries by boundarytype and hierarchy Type'),'default');