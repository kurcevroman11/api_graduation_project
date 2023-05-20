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
	Name integer,
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



CREATE TABLE public.team (
	id serial NOT NULL,
	Ussers integer,
	Task integer,
	Evaluation TIME,
	Times TIME,
	CONSTRAINT Team_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE public.person (
	id serial NOT NULL,
	surname varchar NOT NULL,
	name varchar NOT NULL,
	patronymic varchar,
	type_of_activity integer,
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


ALTER TABLE comments ADD CONSTRAINT Comments_fk0 FOREIGN KEY (Usser) REFERENCES usersRoleProject(id);


ALTER TABLE team ADD CONSTRAINT Team_fk0 FOREIGN KEY (Ussers) REFERENCES usersRoleProject(id);
ALTER TABLE team ADD CONSTRAINT Team_fk1 FOREIGN KEY (Task) REFERENCES task(id);

ALTER TABLE person ADD CONSTRAINT Person_fk0 FOREIGN KEY (type_of_activity) REFERENCES type_of_activity(id);