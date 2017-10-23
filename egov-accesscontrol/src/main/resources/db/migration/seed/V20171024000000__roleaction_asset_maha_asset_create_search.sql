INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),'Asset Service Maha', 'Asset Service Maha', true, 'asset-services-maha', 'Asset Service Maha', 1, null, 'default');

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'CreateAssetServiceMaha','/assets/_create','Asset Service',null,(select id from service where name='Asset Service Maha' and tenantId='default'),1,'Create Asset Service',false,1,now(),1,now());

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ViewAssetServiceMaha','/assets/_search','Asset Service',null,(select id from service where name='Asset Service Maha'and tenantId='default'),1,'View Asset Service',false,1,now(),1,now());
