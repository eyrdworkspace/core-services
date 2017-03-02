CREATE TABLE eg_community (
	id BIGINT NOT NULL,
	name CHARACTER VARYING(100) NOT NULL,
	description CHARACTER VARYING(250),
	active BOOLEAN NOT NULL,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_eg_community PRIMARY KEY (id),
	CONSTRAINT uk_eg_community_name UNIQUE KEY (name)
);

CREATE SEQUENCE seq_eg_community
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;