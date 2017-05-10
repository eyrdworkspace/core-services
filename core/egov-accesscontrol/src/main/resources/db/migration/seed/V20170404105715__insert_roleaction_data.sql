insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get all ReceivingMode'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get all CompaintTypeCategory'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get ComplaintType by type,count and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get all ReceivingCenters'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get ComplaintType by type,categoryId and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get location by LocationName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Search Boundary by BoundryTypeName and HierarchyTypeName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get ComplaintType by type and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get all Statuses'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get next statuses by CurrentStatus and Role'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get Workflow History'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get ChildBoundary by BoundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get Department by code and id'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get File by FileStoreId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get Department by DepartmentId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get Assignments by DepartmentId Or DesignationId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Seva'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Search Boundary by boundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Complaint Registration'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='SearchComplaintFormOfficial'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='UpdateComplaintForm'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get all ReceivingMode'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get all CompaintTypeCategory'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get ComplaintType by type,count and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get all ReceivingCenters'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get ComplaintType by type,categoryId and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get location by LocationName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Search Boundary by BoundryTypeName and HierarchyTypeName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get ComplaintType by type and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get all Statuses'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get next statuses by CurrentStatus and Role'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get Workflow History'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get ChildBoundary by BoundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get Department by code and id'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get File by FileStoreId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get Department by DepartmentId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get Assignments by DepartmentId Or DesignationId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Seva'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Search Boundary by boundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Complaint Registration'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='SearchComplaintFormOfficial'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='UpdateComplaintForm'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get all ReceivingMode'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get all CompaintTypeCategory'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get ComplaintType by type,count and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get all ReceivingCenters'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get ComplaintType by type,categoryId and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get location by LocationName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Search Boundary by BoundryTypeName and HierarchyTypeName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get ComplaintType by type and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get all Statuses'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get next statuses by CurrentStatus and Role'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get Workflow History'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get ChildBoundary by BoundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get Department by code and id'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get File by FileStoreId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get Department by DepartmentId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get Assignments by DepartmentId Or DesignationId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Seva'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Search Boundary by boundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Complaint Registration'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='SearchComplaintFormOfficial'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='UpdateComplaintForm'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get all ReceivingMode'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get all CompaintTypeCategory'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get ComplaintType by type,count and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get all ReceivingCenters'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get ComplaintType by type,categoryId and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get location by LocationName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Search Boundary by BoundryTypeName and HierarchyTypeName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get ComplaintType by type and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get all Statuses'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get next statuses by CurrentStatus and Role'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get Workflow History'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get ChildBoundary by BoundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get Department by code and id'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get File by FileStoreId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get Department by DepartmentId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get Assignments by DepartmentId Or DesignationId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Seva'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Search Boundary by boundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Complaint Registration'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='SearchComplaintFormOfficial'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='UpdateComplaintForm'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get all ReceivingMode'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get all CompaintTypeCategory'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get ComplaintType by type,count and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get all ReceivingCenters'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get ComplaintType by type,categoryId and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get location by LocationName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Search Boundary by BoundryTypeName and HierarchyTypeName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get ComplaintType by type and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get all Statuses'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get next statuses by CurrentStatus and Role'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get Workflow History'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get ChildBoundary by BoundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get Department by code and id'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get File by FileStoreId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get Department by DepartmentId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get Assignments by DepartmentId Or DesignationId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Seva'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Search Boundary by boundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Complaint Registration'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='SearchComplaintFormOfficial'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='UpdateComplaintForm'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get all ReceivingMode'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get all CompaintTypeCategory'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get ComplaintType by type,count and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get all ReceivingCenters'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get ComplaintType by type,categoryId and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get location by LocationName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Search Boundary by BoundryTypeName and HierarchyTypeName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get ComplaintType by type and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get all Statuses'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get next statuses by CurrentStatus and Role'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get Workflow History'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get ChildBoundary by BoundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get Department by code and id'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get File by FileStoreId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get Department by DepartmentId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get Assignments by DepartmentId Or DesignationId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Seva'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Search Boundary by boundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Complaint Registration'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='SearchComplaintFormOfficial'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='UpdateComplaintForm'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get all ReceivingMode'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get all CompaintTypeCategory'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get ComplaintType by type,count and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get all ReceivingCenters'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get ComplaintType by type,categoryId and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get location by LocationName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Search Boundary by BoundryTypeName and HierarchyTypeName'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get ComplaintType by type and tenantId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get all Statuses'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get next statuses by CurrentStatus and Role'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get Workflow History'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get ChildBoundary by BoundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get Department by code and id'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get File by FileStoreId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get Department by DepartmentId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get Assignments by DepartmentId Or DesignationId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Seva'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Search Boundary by boundaryId'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Complaint Registration'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='SearchComplaintFormOfficial'),'ap.public');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='UpdateComplaintForm'),'ap.public');
