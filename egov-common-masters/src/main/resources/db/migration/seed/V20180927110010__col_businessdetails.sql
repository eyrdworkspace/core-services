insert into eg_businesscategory(id,name,code,active,tenantid,version,createdby,lastmodifiedby,createddate,lastmodifieddate) values(
nextval('seq_eg_businesscategory'),'Advertisement Tax','AT',true,'pb.amritsar',0,1,1,1533271684996,1533271684996);
insert into eg_businesscategory(id,name,code,active,tenantid,version,createdby,lastmodifiedby,createddate,lastmodifieddate) values(
nextval('seq_eg_businesscategory'),'Water Tax','WT',true,'pb.amritsar',0,1,1,153327168499,1533271684996);
insert into eg_businesscategory(id,name,code,active,tenantid,version,createdby,lastmodifiedby,createddate,lastmodifieddate) values(
nextval('seq_eg_businesscategory'),'Property Tax','PT',true,'pb.amritsar',0,1,1,1533271684996,1533271684996);
insert into eg_businesscategory(id,name,code,active,tenantid,version,createdby,lastmodifiedby,createddate,lastmodifieddate) values(
nextval('seq_eg_businesscategory'),'Trade License','TL',true,'pb.amritsar',0,1,1,1533271684996,1533271684996);

insert into eg_businessdetails(id,name,businessurl,isenabled,code,businesstype,fund,function,department,vouchercreation,
businesscategory,isvoucherapproved,createdby,
lastmodifiedby,ordernumber,version,tenantid,callbackforapportioning,createddate,lastmodifieddate)
values(nextval('seq_eg_businessdetails'),'Property Tax-Residential','/receipts/receipt-create.action',true,'PTRES','ADHOC','01','909100','DEPT_1',false,
(select id from eg_businesscategory where code='PT' and tenantid='pb.amritsar'),false,1,1,1,0,'pb.amritsar',false,1533271684996,1533271684996);
insert into eg_businessdetails(id,name,businessurl,isenabled,code,businesstype,fund,function,department,vouchercreation,
businesscategory,isvoucherapproved,createdby,
lastmodifiedby,ordernumber,version,tenantid,callbackforapportioning,createddate,lastmodifieddate)
values(nextval('seq_eg_businessdetails'),'Property Tax-Commercial','/receipts/receipt-create.action',true,'PTCOM','ADHOC','01','909100','DEPT_1',false,
(select id from eg_businesscategory where code='PT' and tenantid='pb.amritsar'),false,1,1,1,0,'pb.amritsar',false,1533271684996,1533271684996);
insert into eg_businessdetails(id,name,businessurl,isenabled,code,businesstype,fund,function,department,vouchercreation,
businesscategory,isvoucherapproved,createdby,
lastmodifiedby,ordernumber,version,tenantid,callbackforapportioning,createddate,lastmodifieddate)
values(nextval('seq_eg_businessdetails'),'Property Tax-Vacant Land','/receipts/receipt-create.action',true,'PTVL','ADHOC','01','909100','DEPT_1',false,
(select id from eg_businesscategory where code='PT' and tenantid='pb.amritsar'),false,1,1,1,0,'pb.amritsar',false,1533271684996,1533271684996);
insert into eg_businessdetails(id,name,businessurl,isenabled,code,businesstype,fund,function,department,vouchercreation,
businesscategory,isvoucherapproved,createdby,
lastmodifiedby,ordernumber,version,tenantid,callbackforapportioning,createddate,lastmodifieddate)
values(nextval('seq_eg_businessdetails'),'Advt Tax on hoardings','/receipts/receipt-create.action',true,'ATH','ADHOC','01','909100','DEPT_1',false,
(select id from eg_businesscategory where code='AT' and tenantid='pb.amritsar'),false,1,1,1,0,'pb.amritsar',false,1533271684996,1533271684996);
insert into eg_businessdetails(id,name,businessurl,isenabled,code,businesstype,fund,function,department,vouchercreation,
businesscategory,isvoucherapproved,createdby,
lastmodifiedby,ordernumber,version,tenantid,callbackforapportioning,createddate,lastmodifieddate)
values(nextval('seq_eg_businessdetails'),'Renewal of License','/receipts/receipt-create.action',true,'TLR','ADHOC','01','909100','DEPT_1',false,
(select id from eg_businesscategory where code='TL' and tenantid='pb.amritsar'),false,1,1,1,0,'pb.amritsar',false,1533271684996,1533271684996);
insert into eg_businessdetails(id,name,businessurl,isenabled,code,businesstype,fund,function,department,vouchercreation,
businesscategory,isvoucherapproved,createdby,
lastmodifiedby,ordernumber,version,tenantid,callbackforapportioning,createddate,lastmodifieddate)
values(nextval('seq_eg_businessdetails'),'New Trade License','/receipts/receipt-create.action',true,'TLN','ADHOC','01','909100','DEPT_1',false,
(select id from eg_businesscategory where code='TL' and tenantid='pb.amritsar'),false,1,1,1,0,'pb.amritsar',false,1533271684996,1533271684996);


insert into eg_business_accountdetails(id,businessdetails,chartofaccount,tenantid)
 values(nextval('seq_eg_business_accountdetails'),(select id from eg_businessdetails where name='PTRES' and tenantid='pb.amritsar'),'1100101','pb.amritsar');

insert into eg_business_accountdetails(id,businessdetails,chartofaccount,tenantid)
 values(nextval('seq_eg_business_accountdetails'),(select id from eg_businessdetails where name='PTCOM' and tenantid='pb.amritsar'),'1100103','pb.amritsar');

insert into eg_business_accountdetails(id,businessdetails,chartofaccount,tenantid)
 values(nextval('seq_eg_business_accountdetails'),(select id from eg_businessdetails where name='PTVL' and tenantid='pb.amritsar'),'1100102','pb.amritsar');

insert into eg_business_accountdetails(id,businessdetails,chartofaccount,tenantid)
 values(nextval('seq_eg_business_accountdetails'),(select id from eg_businessdetails where name='ATH' and tenantid='pb.amritsar'),'1100201','pb.amritsar');
