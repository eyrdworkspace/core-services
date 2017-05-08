CREATE TABLE egeis_recruitmentMode (
	id BIGINT NOT NULL,
	name CHARACTER VARYING(50) NOT NULL,
	description CHARACTER VARYING(250),
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_recruitmentmode PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egeis_recruitmentMode
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;