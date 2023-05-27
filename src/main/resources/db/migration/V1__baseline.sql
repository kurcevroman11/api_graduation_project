CREATE TABLE public.usser (
	id serial NOT NULL,
	login varchar NOT NULL,
	password varchar NOT NULL,
	token_short TEXT NOT NULL,
	token_long TEXT NOT NULL,
	personID integer,
	CONSTRAINT Users_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);


CREATE TABLE public.task (
	id serial NOT NULL,
	Name varchar,
	Status integer,
	Start_data DATE,
	Score TIME,
	DescriptionID integer,
	Parent integer,
	Generation integer,
	CommentsID integer,
	CONSTRAINT Task_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);


CREATE TABLE public.status (
	id serial NOT NULL,
	Name text,
	CONSTRAINT Status_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);


CREATE TABLE public.role (
	id serial NOT NULL,
	Name varchar,
	CONSTRAINT Role_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE public.usersRoleProject (
	id serial NOT NULL,
	UserID integer,
	RoleID integer,
	ProjectID integer,
	Type_of_activityID integer,
	score integer,
	CONSTRAINT UsersRoleProject_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);

CREATE TABLE public.description (
	id serial NOT NULL,
	content TEXT,
	File_resources bytea,
	Photo_resources bytea,
	Video_resources bytea,
	CONSTRAINT Descriptions_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);


CREATE TABLE public.comments (
	id serial NOT NULL,
	Usser integer,
	Comments TEXT,
	CONSTRAINT Comments_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);


CREATE TABLE public.type_of_activity (
	id serial NOT NULL,
	Name varchar,
	CONSTRAINT Type_of_activity_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);


CREATE TABLE public.person (
	id serial NOT NULL,
	surname varchar NOT NULL,
	name varchar NOT NULL,
	patronymic varchar,
	CONSTRAINT person_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);


ALTER TABLE usser ADD CONSTRAINT Users_fk0 FOREIGN KEY (personID) REFERENCES person(id);

ALTER TABLE task ADD CONSTRAINT Task_fk0 FOREIGN KEY (Status) REFERENCES status(id);
ALTER TABLE task ADD CONSTRAINT Task_fk1 FOREIGN KEY (DescriptionID) REFERENCES description(id);
ALTER TABLE task ADD CONSTRAINT Task_fk2 FOREIGN KEY (CommentsID) REFERENCES comments(id);

ALTER TABLE usersRoleProject ADD CONSTRAINT UsersRoleProject_fk0 FOREIGN KEY (UserID) REFERENCES usser(id);
ALTER TABLE usersRoleProject ADD CONSTRAINT UsersRoleProject_fk1 FOREIGN KEY (RoleID) REFERENCES role(id);
ALTER TABLE usersRoleProject ADD CONSTRAINT UsersRoleProject_fk2 FOREIGN KEY (ProjectID) REFERENCES task(id);
ALTER TABLE usersRoleProject ADD CONSTRAINT UsersRoleProject_fk3 FOREIGN KEY (Type_of_activityID) REFERENCES type_of_activity(id);

ALTER TABLE comments ADD CONSTRAINT Comments_fk0 FOREIGN KEY (Usser) REFERENCES usersRoleProject(id);
