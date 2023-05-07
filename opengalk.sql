--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2 (Debian 15.2-1.pgdg110+1)
-- Dumped by pg_dump version 15.2 (Debian 15.2-1.pgdg110+1)

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

--
-- Name: college; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA college;


ALTER SCHEMA college OWNER TO postgres;

--
-- Name: paper; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA paper;


ALTER SCHEMA paper OWNER TO postgres;

--
-- Name: user; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "user";


ALTER SCHEMA "user" OWNER TO postgres;

--
-- Name: college_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.college_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.college_info_id_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: college_info; Type: TABLE; Schema: college; Owner: postgres
--

CREATE TABLE college.college_info (
    id bigint DEFAULT nextval('public.college_info_id_seq'::regclass) NOT NULL,
    college_name character varying NOT NULL,
    create_time timestamp without time zone NOT NULL,
    is_deleted smallint DEFAULT 0 NOT NULL,
    remark character varying
);


ALTER TABLE college.college_info OWNER TO postgres;

--
-- Name: 0877d41ba06447e38667756a45c2e5ea; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper."0877d41ba06447e38667756a45c2e5ea" (
    id smallint NOT NULL,
    type smallint NOT NULL,
    subject character varying NOT NULL,
    picture character varying,
    option_a character varying NOT NULL,
    option_b character varying NOT NULL,
    option_c character varying NOT NULL,
    option_d character varying NOT NULL,
    answer character varying NOT NULL
);


ALTER TABLE paper."0877d41ba06447e38667756a45c2e5ea" OWNER TO postgres;

--
-- Name: 14a266a743ac47e4bb3ea4e01323a98e; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper."14a266a743ac47e4bb3ea4e01323a98e" (
    id smallint NOT NULL,
    type smallint NOT NULL,
    subject character varying NOT NULL,
    picture character varying,
    option_a character varying NOT NULL,
    option_b character varying NOT NULL,
    option_c character varying NOT NULL,
    option_d character varying NOT NULL,
    answer character varying NOT NULL
);


ALTER TABLE paper."14a266a743ac47e4bb3ea4e01323a98e" OWNER TO postgres;

--
-- Name: paper_collect; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper.paper_collect (
    uuid character varying NOT NULL,
    subject_id smallint NOT NULL,
    user_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    id character varying NOT NULL
);


ALTER TABLE paper.paper_collect OWNER TO postgres;

--
-- Name: COLUMN paper_collect.uuid; Type: COMMENT; Schema: paper; Owner: postgres
--

COMMENT ON COLUMN paper.paper_collect.uuid IS '试卷uuid';


--
-- Name: COLUMN paper_collect.subject_id; Type: COMMENT; Schema: paper; Owner: postgres
--

COMMENT ON COLUMN paper.paper_collect.subject_id IS '题目序号';


--
-- Name: COLUMN paper_collect.user_id; Type: COMMENT; Schema: paper; Owner: postgres
--

COMMENT ON COLUMN paper.paper_collect.user_id IS '用户id';


--
-- Name: paper_correct; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper.paper_correct (
    uuid character varying NOT NULL,
    subject_id smallint NOT NULL,
    user_id bigint NOT NULL,
    correct_text text NOT NULL,
    create_time timestamp without time zone NOT NULL,
    id character varying NOT NULL
);


ALTER TABLE paper.paper_correct OWNER TO postgres;

--
-- Name: paper_info; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper.paper_info (
    id character varying NOT NULL,
    name character varying NOT NULL,
    remark character varying NOT NULL,
    create_by bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    type smallint NOT NULL,
    is_deleted smallint DEFAULT 0 NOT NULL
);


ALTER TABLE paper.paper_info OWNER TO postgres;

--
-- Name: paper_record; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper.paper_record (
    id character varying NOT NULL,
    user_id bigint NOT NULL,
    score smallint,
    create_time timestamp without time zone NOT NULL,
    answer character varying
);


ALTER TABLE paper.paper_record OWNER TO postgres;

--
-- Name: paper_collect_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.paper_collect_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.paper_collect_id_seq OWNER TO postgres;

--
-- Name: user_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_info_id_seq OWNER TO postgres;

--
-- Name: user_info; Type: TABLE; Schema: user; Owner: postgres
--

CREATE TABLE "user".user_info (
    id bigint DEFAULT nextval('public.user_info_id_seq'::regclass) NOT NULL,
    account character varying NOT NULL,
    password character varying NOT NULL,
    authority smallint DEFAULT 2 NOT NULL,
    is_deleted smallint DEFAULT 0 NOT NULL,
    create_time timestamp without time zone NOT NULL,
    create_by bigint,
    is_locked smallint DEFAULT 0 NOT NULL,
    name character varying,
    phone_number character varying,
    college_id smallint,
    email character varying,
    gender smallint,
    birthday date
);


ALTER TABLE "user".user_info OWNER TO postgres;

--
-- Name: user_login_and_logout_record; Type: TABLE; Schema: user; Owner: postgres
--

CREATE TABLE "user".user_login_and_logout_record (
    id bigint NOT NULL,
    login_or_logout smallint NOT NULL,
    "time" timestamp without time zone NOT NULL
);


ALTER TABLE "user".user_login_and_logout_record OWNER TO postgres;

--
-- Name: user_update_record; Type: TABLE; Schema: user; Owner: postgres
--

CREATE TABLE "user".user_update_record (
    id bigint NOT NULL,
    table_name character varying NOT NULL,
    column_name character varying NOT NULL,
    before_update character varying,
    after_update character varying,
    update_time timestamp without time zone NOT NULL,
    update_by bigint NOT NULL,
    remark character varying
);


ALTER TABLE "user".user_update_record OWNER TO postgres;

--
-- Data for Name: college_info; Type: TABLE DATA; Schema: college; Owner: postgres
--

COPY college.college_info (id, college_name, create_time, is_deleted, remark) FROM stdin;
1	tdjcxy	2023-04-23 19:14:10.322864	0	tdjcxy
\.


--
-- Data for Name: 0877d41ba06447e38667756a45c2e5ea; Type: TABLE DATA; Schema: paper; Owner: postgres
--

COPY paper."0877d41ba06447e38667756a45c2e5ea" (id, type, subject, picture, option_a, option_b, option_c, option_d, answer) FROM stdin;
0	0	1．某甲是间歇性精神病人。某日，某甲喝醉了酒，把酒店老板打成重伤，在群众抓捕他时，某甲因惊恐而精神病发作，则某甲（ ）。	\N	应当负刑事责任，但可以减轻处罚	应当负刑事责任，但可以从轻处罚	不负刑事责任，因其是精神病人	应当负刑事责任	A
1	1	2．中学生某甲，2002年4月5日生。2017年3月28日，他故意把一同学打成重伤，某甲对于他的这一行为（ ）。	\N	应负刑事责任，但应当从轻或减轻处罚	应负刑事责任，但可以从轻或减轻处罚	应负刑事责任，但可以减轻或免除处罚	不负刑事责任	AC
2	0	3．某甲是间歇性精神病人。某日，某甲喝醉了酒，把酒店老板打成重伤，在群众抓捕他时，某甲因惊恐而精神病发作，则某甲（ ）。	\N	应当负刑事责任，但可以减轻处罚	应当负刑事责任，但可以从轻处罚	不负刑事责任，因其是精神病人	应当负刑事责任	A
3	1	4．中学生某甲，2002年4月5日生。2017年3月28日，他故意把一同学打成重伤，某甲对于他的这一行为（ ）。	\N	应负刑事责任，但应当从轻或减轻处罚	应负刑事责任，但可以从轻或减轻处罚	应负刑事责任，但可以减轻或免除处罚	不负刑事责任	AC
\.


--
-- Data for Name: 14a266a743ac47e4bb3ea4e01323a98e; Type: TABLE DATA; Schema: paper; Owner: postgres
--

COPY paper."14a266a743ac47e4bb3ea4e01323a98e" (id, type, subject, picture, option_a, option_b, option_c, option_d, answer) FROM stdin;
0	0	1．某甲是间歇性精神病人。某日，某甲喝醉了酒，把酒店老板打成重伤，在群众抓捕他时，某甲因惊恐而精神病发作，则某甲（ ）。	\N	应当负刑事责任，但可以减轻处罚	应当负刑事责任，但可以从轻处罚	不负刑事责任，因其是精神病人	应当负刑事责任	A
1	0	2．中学生某甲，2002年4月5日生。2017年3月28日，他故意把一同学打成重伤，某甲对于他的这一行为（ ）。	\N	应负刑事责任，但应当从轻或减轻处罚	应负刑事责任，但可以从轻或减轻处罚	应负刑事责任，但可以减轻或免除处罚	不负刑事责任	A
\.


--
-- Data for Name: paper_collect; Type: TABLE DATA; Schema: paper; Owner: postgres
--

COPY paper.paper_collect (uuid, subject_id, user_id, create_time, id) FROM stdin;
0877d41ba06447e38667756a45c2e5ea	1	1	2023-05-04 21:56:56.739483	1_0877d41ba06447e38667756a45c2e5ea_1
\.


--
-- Data for Name: paper_correct; Type: TABLE DATA; Schema: paper; Owner: postgres
--

COPY paper.paper_correct (uuid, subject_id, user_id, correct_text, create_time, id) FROM stdin;
0877d41ba06447e38667756a45c2e5ea	1	1	1111111111111111	2023-05-04 21:57:03.535284	1_0877d41ba06447e38667756a45c2e5ea_1
\.


--
-- Data for Name: paper_info; Type: TABLE DATA; Schema: paper; Owner: postgres
--

COPY paper.paper_info (id, name, remark, create_by, create_time, type, is_deleted) FROM stdin;
14a266a743ac47e4bb3ea4e01323a98e	321	123	1	2023-04-24 17:06:39.476237	0	0
0877d41ba06447e38667756a45c2e5ea	测试1	123456	1	2023-04-23 15:54:57.42424	0	0
\.


--
-- Data for Name: paper_record; Type: TABLE DATA; Schema: paper; Owner: postgres
--

COPY paper.paper_record (id, user_id, score, create_time, answer) FROM stdin;
0877d41ba06447e38667756a45c2e5ea	1	1	2023-04-29 12:05:08.537852	D,AB,A,BA,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
\.


--
-- Data for Name: user_info; Type: TABLE DATA; Schema: user; Owner: postgres
--

COPY "user".user_info (id, account, password, authority, is_deleted, create_time, create_by, is_locked, name, phone_number, college_id, email, gender, birthday) FROM stdin;
2	cxcxcx	$2a$10$9Zyyxsg/acx/a44AeFXz9uDTUbfQ/5bzk6sSap0pimvQjfbQVTe5K	2	0	2023-04-19 21:53:16.109667	1	0	xxx	123456	1	123@123.com	1	2023-04-25
1	admin	$2a$10$kUZMgxF8x/c3lwU9urIdbe7Wh.I70JFPysEggq/dqdQ98g3orVtzq	0	0	2023-04-12 22:41:03	1	0	cx	123456	\N	123@123.com	0	2023-04-25
\.


--
-- Data for Name: user_login_and_logout_record; Type: TABLE DATA; Schema: user; Owner: postgres
--

COPY "user".user_login_and_logout_record (id, login_or_logout, "time") FROM stdin;
1	0	2023-04-12 22:44:23.007997
1	0	2023-04-19 21:10:58.666908
1	0	2023-04-19 21:37:47.926205
1	1	2023-04-23 18:13:53.937695
1	0	2023-04-23 18:14:08.445702
1	1	2023-04-23 22:11:40.331397
1	0	2023-04-23 22:11:57.020113
1	0	2023-04-26 15:02:33.460006
1	1	2023-05-03 17:22:59.235646
1	0	2023-05-03 17:24:28.919163
1	0	2023-05-04 11:20:53.150643
\.


--
-- Data for Name: user_update_record; Type: TABLE DATA; Schema: user; Owner: postgres
--

COPY "user".user_update_record (id, table_name, column_name, before_update, after_update, update_time, update_by, remark) FROM stdin;
1	user.user_info	用户信息	{"authority":0,"isLocked":0}	{"id":1,"authority":0,"isLocked":0,"name":"cx","phoneNumber":"123456","email":"123@123.com","gender":0,"birthday":1682352000000}	2023-04-19 21:16:43.680428	1	修改个人信息
2	user.user_info	all	\N	\N	2023-04-19 21:53:16.118355	1	添加账号
2	user.user_info	用户信息	{"authority":2,"isLocked":0}	{"id":2,"authority":2,"isLocked":0,"name":"xxx","phoneNumber":"123456","email":"123@123.com","gender":1,"birthday":1682352000000}	2023-04-22 16:39:30.942154	1	修改个人信息
2	user.user_info	用户信息	{"authority":2,"isLocked":0,"name":"xxx","phoneNumber":"123456","email":"123@123.com","gender":1,"birthday":1682352000000}	{"id":2,"authority":2,"isLocked":0,"name":"xxx","phoneNumber":"123456","collegeId":1,"email":"123@123.com","gender":1,"birthday":1682352000000}	2023-04-26 11:05:13.990119	1	修改个人信息
\.


--
-- Name: college_info_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.college_info_id_seq', 1, true);


--
-- Name: paper_collect_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.paper_collect_id_seq', 1, false);


--
-- Name: user_info_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_info_id_seq', 2, true);


--
-- Name: college_info college_info_pk; Type: CONSTRAINT; Schema: college; Owner: postgres
--

ALTER TABLE ONLY college.college_info
    ADD CONSTRAINT college_info_pk PRIMARY KEY (id);


--
-- Name: 0877d41ba06447e38667756a45c2e5ea 0877d41ba06447e38667756a45c2e5ea_pkey; Type: CONSTRAINT; Schema: paper; Owner: postgres
--

ALTER TABLE ONLY paper."0877d41ba06447e38667756a45c2e5ea"
    ADD CONSTRAINT "0877d41ba06447e38667756a45c2e5ea_pkey" PRIMARY KEY (id);


--
-- Name: 14a266a743ac47e4bb3ea4e01323a98e gazykm_14a266a743ac47e4bb3ea4e01323a98e_pkey; Type: CONSTRAINT; Schema: paper; Owner: postgres
--

ALTER TABLE ONLY paper."14a266a743ac47e4bb3ea4e01323a98e"
    ADD CONSTRAINT gazykm_14a266a743ac47e4bb3ea4e01323a98e_pkey PRIMARY KEY (id);


--
-- Name: paper_collect paper_collect_pk; Type: CONSTRAINT; Schema: paper; Owner: postgres
--

ALTER TABLE ONLY paper.paper_collect
    ADD CONSTRAINT paper_collect_pk PRIMARY KEY (id);


--
-- Name: paper_correct paper_correct_pk; Type: CONSTRAINT; Schema: paper; Owner: postgres
--

ALTER TABLE ONLY paper.paper_correct
    ADD CONSTRAINT paper_correct_pk PRIMARY KEY (id);


--
-- Name: paper_info paper_info_pk; Type: CONSTRAINT; Schema: paper; Owner: postgres
--

ALTER TABLE ONLY paper.paper_info
    ADD CONSTRAINT paper_info_pk PRIMARY KEY (id);


--
-- Name: user_info user_info_pk; Type: CONSTRAINT; Schema: user; Owner: postgres
--

ALTER TABLE ONLY "user".user_info
    ADD CONSTRAINT user_info_pk PRIMARY KEY (id);


--
-- Name: user_login_and_logout_record_id_index; Type: INDEX; Schema: user; Owner: postgres
--

CREATE INDEX user_login_and_logout_record_id_index ON "user".user_login_and_logout_record USING btree (id);


--
-- Name: user_update_record_id_index; Type: INDEX; Schema: user; Owner: postgres
--

CREATE INDEX user_update_record_id_index ON "user".user_update_record USING btree (id);


--
-- PostgreSQL database dump complete
--

