--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
    id integer NOT NULL,
    usser integer,
    comments text,
    taskid integer
);


ALTER TABLE public.comments OWNER TO postgres;

--
-- Name: comments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.comments_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.comments_id_seq OWNER TO postgres;

--
-- Name: comments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.comments_id_seq OWNED BY public.comments.id;


--
-- Name: description; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.description (
    id integer NOT NULL,
    content text,
    file_resources text,
    photo_resources text,
    video_resources text
);


ALTER TABLE public.description OWNER TO postgres;

--
-- Name: description_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.description_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.description_id_seq OWNER TO postgres;

--
-- Name: description_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.description_id_seq OWNED BY public.description.id;


--
-- Name: excel_file; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.excel_file (
    id integer NOT NULL,
    name_plan character varying,
    path character varying,
    projectid integer
);


ALTER TABLE public.excel_file OWNER TO postgres;

--
-- Name: excel_file_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.excel_file_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.excel_file_id_seq OWNER TO postgres;

--
-- Name: excel_file_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.excel_file_id_seq OWNED BY public.excel_file.id;
--
-- Name: excel_file; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.excel_file (
    id integer NOT NULL,
    name_plan character varying,
    path character varying,
    projectid integer
);


ALTER TABLE public.excel_file OWNER TO postgres;

--
-- Name: excel_file_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.excel_file_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.excel_file_id_seq OWNER TO postgres;

--
-- Name: excel_file_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.excel_file_id_seq OWNED BY public.excel_file.id;


--
-- Name: usersroleproject; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usersroleproject (
    id integer NOT NULL,
    userid integer,
    roleid integer,
    projectid integer,
    type_of_activityid integer,
    score integer,
    current_task_id integer
);


ALTER TABLE public.usersroleproject OWNER TO postgres;

--
-- Name: usersroleproject_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usersroleproject_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usersroleproject_id_seq OWNER TO postgres;

--
-- Name: usersroleproject_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usersroleproject_id_seq OWNED BY public.usersroleproject.id;


--
-- Name: file; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.file (
    id integer DEFAULT nextval('public.usersroleproject_id_seq'::regclass) NOT NULL,
    orig_filename text,
    type text,
    "taskId" integer
);


ALTER TABLE public.file OWNER TO postgres;

--
-- Name: person; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person (
    id integer NOT NULL,
    surname character varying NOT NULL,
    name character varying NOT NULL,
    patronymic character varying
);


ALTER TABLE public.person OWNER TO postgres;

--
-- Name: person_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.person_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.person_id_seq OWNER TO postgres;

--
-- Name: person_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.person_id_seq OWNED BY public.person.id;


--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id integer NOT NULL,
    name character varying
);


ALTER TABLE public.role OWNER TO postgres;

--
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.role_id_seq OWNER TO postgres;

--
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;


--
-- Name: status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.status (
    id integer NOT NULL,
    name text
);


ALTER TABLE public.status OWNER TO postgres;

--
-- Name: status_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.status_id_seq OWNER TO postgres;

--
-- Name: status_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.status_id_seq OWNED BY public.status.id;


--
-- Name: task; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.task (
    id integer NOT NULL,
    name character varying,
    status integer,
    start_data timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    descriptionid integer,
    parent integer,
    score integer,
    generation integer,
    typeofactivityid integer,
    "position" integer,
    gruop integer,
    dependence integer
);


ALTER TABLE public.task OWNER TO postgres;

--
-- Name: task_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.task_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.task_id_seq OWNER TO postgres;

--
-- Name: task_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.task_id_seq OWNED BY public.task.id;


--
-- Name: type_of_activity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.type_of_activity (
    id integer NOT NULL,
    name character varying
);


ALTER TABLE public.type_of_activity OWNER TO postgres;

--
-- Name: type_of_activity_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.type_of_activity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.type_of_activity_id_seq OWNER TO postgres;

--
-- Name: type_of_activity_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.type_of_activity_id_seq OWNED BY public.type_of_activity.id;


--
-- Name: usser; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usser (
    id integer NOT NULL,
    login character varying NOT NULL,
    password character varying NOT NULL,
    token_long text,
    personid integer
);


ALTER TABLE public.usser OWNER TO postgres;

--
-- Name: usser_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usser_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usser_id_seq OWNER TO postgres;

--
-- Name: usser_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usser_id_seq OWNED BY public.usser.id;


--
-- Name: comments id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments ALTER COLUMN id SET DEFAULT nextval('public.comments_id_seq'::regclass);


--
-- Name: description id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.description ALTER COLUMN id SET DEFAULT nextval('public.description_id_seq'::regclass);


--
-- Name: excel_file id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.excel_file ALTER COLUMN id SET DEFAULT nextval('public.excel_file_id_seq'::regclass);


--
-- Name: person id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person ALTER COLUMN id SET DEFAULT nextval('public.person_id_seq'::regclass);


--
-- Name: role id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);


--
-- Name: status id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.status ALTER COLUMN id SET DEFAULT nextval('public.status_id_seq'::regclass);


--
-- Name: task id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task ALTER COLUMN id SET DEFAULT nextval('public.task_id_seq'::regclass);


--
-- Name: type_of_activity id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.type_of_activity ALTER COLUMN id SET DEFAULT nextval('public.type_of_activity_id_seq'::regclass);


--
-- Name: usersroleproject id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usersroleproject ALTER COLUMN id SET DEFAULT nextval('public.usersroleproject_id_seq'::regclass);


--
-- Name: usser id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usser ALTER COLUMN id SET DEFAULT nextval('public.usser_id_seq'::regclass);


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comments (id, usser, comments, taskid) FROM stdin;
\.


--
-- Data for Name: description; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.description (id, content, file_resources, photo_resources, video_resources) FROM stdin;
18	\N	src\\main\\resources\\media\\18\\file\\	src\\main\\resources\\media\\18\\photo\\	src\\main\\resources\\media\\18\\video\\
19	\N	src\\main\\resources\\media\\19\\file\\	src\\main\\resources\\media\\19\\photo\\	src\\main\\resources\\media\\19\\video\\
20	\N	src\\main\\resources\\media\\20\\file\\	src\\main\\resources\\media\\20\\photo\\	src\\main\\resources\\media\\20\\video\\
21	\N	src\\main\\resources\\media\\23\\file\\	src\\main\\resources\\media\\23\\photo\\	src\\main\\resources\\media\\23\\video\\
22	\N	src\\main\\resources\\media\\24\\file\\	src\\main\\resources\\media\\24\\photo\\	src\\main\\resources\\media\\24\\video\\
23	\N	src\\main\\resources\\media\\25\\file\\	src\\main\\resources\\media\\25\\photo\\	src\\main\\resources\\media\\25\\video\\
24	\N	src\\main\\resources\\media\\27\\file\\	src\\main\\resources\\media\\27\\photo\\	src\\main\\resources\\media\\27\\video\\
25	\N	src\\main\\resources\\media\\28\\file\\	src\\main\\resources\\media\\28\\photo\\	src\\main\\resources\\media\\28\\video\\
26	\N	src\\main\\resources\\media\\29\\file\\	src\\main\\resources\\media\\29\\photo\\	src\\main\\resources\\media\\29\\video\\
27	\N	src\\main\\resources\\media\\30\\file\\	src\\main\\resources\\media\\30\\photo\\	src\\main\\resources\\media\\30\\video\\
28	\N	src\\main\\resources\\media\\31\\file\\	src\\main\\resources\\media\\31\\photo\\	src\\main\\resources\\media\\31\\video\\
29	\N	src\\main\\resources\\media\\32\\file\\	src\\main\\resources\\media\\32\\photo\\	src\\main\\resources\\media\\32\\video\\
30	\N	src\\main\\resources\\media\\33\\file\\	src\\main\\resources\\media\\33\\photo\\	src\\main\\resources\\media\\33\\video\\
31	\N	src\\main\\resources\\media\\34\\file\\	src\\main\\resources\\media\\34\\photo\\	src\\main\\resources\\media\\34\\video\\
32	\N	src\\main\\resources\\media\\35\\file\\	src\\main\\resources\\media\\35\\photo\\	src\\main\\resources\\media\\35\\video\\
33	\N	src\\main\\resources\\media\\36\\file\\	src\\main\\resources\\media\\36\\photo\\	src\\main\\resources\\media\\36\\video\\
34	\N	src\\main\\resources\\media\\37\\file\\	src\\main\\resources\\media\\37\\photo\\	src\\main\\resources\\media\\37\\video\\
\.


--
-- Data for Name: excel_file; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.excel_file (id, name_plan, path, projectid) FROM stdin;
\.


--
-- Data for Name: file; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.file (id, orig_filename, type, "taskId") FROM stdin;
\.


--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person (id, surname, name, patronymic) FROM stdin;
1	Иванов	Иван	Иванович
2	Петров	Пётр	
3	Сидоров	Михаил	
4	Ми	Сидо	
5	Сидо	Ми	
6			
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id, name) FROM stdin;
1	Коментатор
2	Исполнитель
3	Админ
4	Проект менеджер
\.


--
-- Data for Name: status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.status (id, name) FROM stdin;
1	Готово
2	В работе
3	В ожиданнии
\.


--
-- Data for Name: task; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task (id, name, status, start_data, descriptionid, parent, score, generation, typeofactivityid, "position", gruop, dependence) FROM stdin;
<<<<<<< HEAD:src/main/resources/db/migration/V1__Start.sql
29	Разработать API	2	2023-06-16 12:04:29.661011+03	26	28	12	2	1	\N	\N	\N
32	Разработать put запрос	2	2023-06-16 12:08:36.978714+03	29	29	3	3	1	\N	\N	\N
31	Разработать post запрос	2	2023-06-16 12:08:24.750142+03	28	29	3	3	1	\N	\N	\N
30	Разработать get запрос	2	2023-06-16 12:08:08.067671+03	27	29	3	3	1	\N	\N	\N
34	Разработать прототип	2	2023-06-16 13:44:36.203519+03	31	33	6	3	2	\N	\N	\N
35	Разработать что-то	2	2023-06-16 13:45:10.080295+03	32	33	6	3	2	\N	\N	\N
36	продумывние прототипа	2	2023-06-16 13:47:52.232819+03	33	33	6	3	2	\N	\N	\N
33	Разработать дизайн	2	2023-06-16 12:34:05.15122+03	30	28	18	2	2	\N	\N	\N
28	Приложение список дел	2	2023-06-16 11:57:11.168978+03	25	\N	18	1	\N	\N	\N	\N
37	Разработка KMM по отрисовки задач	2	2023-06-16 23:52:52.584454+03	34	\N	0	1	2	\N	\N	\N
=======
19	Разработать дизайн	2	2023-06-12 19:08:27.234395+03	19	18	8	2	3	\N	\N	\N
18	Приложение список дел	2	2023-06-12 19:02:07.416664+03	18	\N	8	1	\N	\N	\N	\N
>>>>>>> master:src/main/resources/db/migration/V1__start.sql
\.


--
-- Data for Name: type_of_activity; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.type_of_activity (id, name) FROM stdin;
1	Backend
2	Frontend
3	UI/UX дизайнер
4	Тестировщик
\.


--
-- Data for Name: usersroleproject; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usersroleproject (id, userid, roleid, projectid, type_of_activityid, score, current_task_id) FROM stdin;
33	3	2	28	1	7	32
34	4	2	28	1	4	31
35	5	2	28	1	5	30
36	6	2	28	2	3	34
37	7	2	28	2	6	35
\.


--
-- Data for Name: usser; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usser (id, login, password, token_long, personid) FROM stdin;
8	user10	666	eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJEZXZlbG9wZXJzIiwiaXNzIjoiQmVlckplc3VzIiwidXNlcm5hbWUiOiJ1c2VyMTAiLCJ1c2VySWQiOjgsImV4cCI6MTY4NzE3OTU3MH0.9anPr6ms99WBDwLN6nSd4-du0c3T-qwv_qTyfsSVM0s	6
5	user3	44	eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJEZXZlbG9wZXJzIiwiaXNzIjoiQmVlckplc3VzIiwidXNlcm5hbWUiOiJ1c2VyMyIsInVzZXJJZCI6NSwiZXhwIjoxNjg3MTg5NzI3fQ.s_vLxjnDD7DLANus8sUL5bR33LXprGPWeIBnVqaDvtQ	3
3	user1	22	eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJEZXZlbG9wZXJzIiwiaXNzIjoiQmVlckplc3VzIiwidXNlcm5hbWUiOiJ1c2VyMSIsInVzZXJJZCI6MywiZXhwIjoxNjg2OTUyODM3fQ.QWOqNFSgxIsOBIWRc53PDk41yw3cIICAloohR_59wFw	1
6	user4	55		4
7	user5	66		5
4	user2	33	eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJEZXZlbG9wZXJzIiwiaXNzIjoiQmVlckplc3VzIiwidXNlcm5hbWUiOiJ1c2VyMiIsInVzZXJJZCI6NCwiZXhwIjoxNjg2NzU2MTYwfQ.Szh5wnBGNXVoP6gYHo6hMbdMyWHPWywPTb1PDpAVDog	2
2	admin	admin123	eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJEZXZlbG9wZXJzIiwiaXNzIjoiQmVlckplc3VzIiwidXNlcm5hbWUiOiJhZG1pbiIsInVzZXJJZCI6MiwiZXhwIjoxNjg3MTczMzgwfQ.y0fEBCqbxA-2bHzZY5qKgsNRyf0aPTAjv-kxqlE8GeI	\N
\.


--
-- Name: comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comments_id_seq', 1, false);


--
-- Name: description_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.description_id_seq', 34, true);


--
-- Name: excel_file_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.excel_file_id_seq', 1, false);


--
-- Name: person_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.person_id_seq', 6, true);


--
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_id_seq', 1, false);


--
-- Name: status_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.status_id_seq', 1, false);


--
-- Name: task_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.task_id_seq', 37, true);


--
-- Name: type_of_activity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.type_of_activity_id_seq', 1, false);


--
-- Name: usersroleproject_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usersroleproject_id_seq', 37, true);


--
-- Name: usser_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usser_id_seq', 8, true);


--
-- Name: comments comments_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pk PRIMARY KEY (id);


--
-- Name: description descriptions_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.description
    ADD CONSTRAINT descriptions_pk PRIMARY KEY (id);


--
-- Name: excel_file excel_file_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.excel_file
    ADD CONSTRAINT excel_file_pkey PRIMARY KEY (id);


--
-- Name: file file_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.file
    ADD CONSTRAINT file_pkey PRIMARY KEY (id);


--
-- Name: person person_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pk PRIMARY KEY (id);


--
-- Name: role role_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pk PRIMARY KEY (id);


--
-- Name: status status_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.status
    ADD CONSTRAINT status_pk PRIMARY KEY (id);


--
-- Name: task task_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT task_pk PRIMARY KEY (id);


--
-- Name: type_of_activity type_of_activity_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.type_of_activity
    ADD CONSTRAINT type_of_activity_pk PRIMARY KEY (id);


--
-- Name: usser users_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usser
    ADD CONSTRAINT users_pk PRIMARY KEY (id);


--
-- Name: usersroleproject usersroleproject_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usersroleproject
    ADD CONSTRAINT usersroleproject_pk PRIMARY KEY (id);


--
-- Name: comments comments_fk0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_fk0 FOREIGN KEY (usser) REFERENCES public.usersroleproject(id);


--
-- Name: comments comments_fk1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_fk1 FOREIGN KEY (taskid) REFERENCES public.task(id);


--
-- Name: excel_file excel_file_projectid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.excel_file
    ADD CONSTRAINT excel_file_projectid_fkey FOREIGN KEY (projectid) REFERENCES public.task(id);


--
-- Name: task task_fk0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT task_fk0 FOREIGN KEY (status) REFERENCES public.status(id);


--
-- Name: task task_fk1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT task_fk1 FOREIGN KEY (descriptionid) REFERENCES public.description(id);


--
-- Name: task task_typeofactivityid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT task_typeofactivityid_fkey FOREIGN KEY (typeofactivityid) REFERENCES public.type_of_activity(id);


--
-- Name: usser users_fk0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usser
    ADD CONSTRAINT users_fk0 FOREIGN KEY (personid) REFERENCES public.person(id);


--
-- Name: usersroleproject usersroleproject_fk0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usersroleproject
    ADD CONSTRAINT usersroleproject_fk0 FOREIGN KEY (userid) REFERENCES public.usser(id);


--
-- Name: usersroleproject usersroleproject_fk1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usersroleproject
    ADD CONSTRAINT usersroleproject_fk1 FOREIGN KEY (roleid) REFERENCES public.role(id);


--
-- Name: usersroleproject usersroleproject_fk2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usersroleproject
    ADD CONSTRAINT usersroleproject_fk2 FOREIGN KEY (projectid) REFERENCES public.task(id);


--
-- Name: usersroleproject usersroleproject_fk3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usersroleproject
    ADD CONSTRAINT usersroleproject_fk3 FOREIGN KEY (type_of_activityid) REFERENCES public.type_of_activity(id);


--
-- PostgreSQL database dump complete
--

