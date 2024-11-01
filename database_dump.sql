--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0
-- Dumped by pg_dump version 17.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: IMPACT Club; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "IMPACT Club";


ALTER SCHEMA "IMPACT Club" OWNER TO postgres;

--
-- Name: isattendence; Type: TYPE; Schema: IMPACT Club; Owner: postgres
--

CREATE TYPE "IMPACT Club".isattendence AS ENUM (
    'Present',
    'Absent'
);


ALTER TYPE "IMPACT Club".isattendence OWNER TO postgres;

--
-- Name: projectstatustype; Type: TYPE; Schema: IMPACT Club; Owner: postgres
--

CREATE TYPE "IMPACT Club".projectstatustype AS ENUM (
    'In Progress',
    'Completed',
    'On Hold'
);


ALTER TYPE "IMPACT Club".projectstatustype OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: leader; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".leader (
    leaderid integer NOT NULL,
    ssn character(9) NOT NULL,
    major character varying(100)
);


ALTER TABLE "IMPACT Club".leader OWNER TO postgres;

--
-- Name: leader_course; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".leader_course (
    leaderid integer NOT NULL,
    courseid integer NOT NULL,
    topic character varying(255) NOT NULL,
    goals text,
    date date,
    duration character varying(20),
    feedback text
);


ALTER TABLE "IMPACT Club".leader_course OWNER TO postgres;

--
-- Name: leader_leaderid_seq; Type: SEQUENCE; Schema: IMPACT Club; Owner: postgres
--

CREATE SEQUENCE "IMPACT Club".leader_leaderid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE "IMPACT Club".leader_leaderid_seq OWNER TO postgres;

--
-- Name: leader_leaderid_seq; Type: SEQUENCE OWNED BY; Schema: IMPACT Club; Owner: postgres
--

ALTER SEQUENCE "IMPACT Club".leader_leaderid_seq OWNED BY "IMPACT Club".leader.leaderid;


--
-- Name: member; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".member (
    memberid integer NOT NULL,
    ssn character(9) NOT NULL,
    personaldevelopmentgoals character varying(255),
    skillsdeveloped character varying(255),
    favoriteactivities character varying(255),
    personal_test character varying(200)
);


ALTER TABLE "IMPACT Club".member OWNER TO postgres;

--
-- Name: member_memberid_seq; Type: SEQUENCE; Schema: IMPACT Club; Owner: postgres
--

CREATE SEQUENCE "IMPACT Club".member_memberid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE "IMPACT Club".member_memberid_seq OWNER TO postgres;

--
-- Name: member_memberid_seq; Type: SEQUENCE OWNED BY; Schema: IMPACT Club; Owner: postgres
--

ALTER SEQUENCE "IMPACT Club".member_memberid_seq OWNED BY "IMPACT Club".member.memberid;


--
-- Name: memberproject; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".memberproject (
    memberid integer NOT NULL,
    projectid integer NOT NULL,
    roleinproject character varying(50),
    projectinvolvementlevel integer,
    projectfeedback text,
    skillsdevelopedinproject text
);


ALTER TABLE "IMPACT Club".memberproject OWNER TO postgres;

--
-- Name: mentor; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".mentor (
    mentorid integer NOT NULL,
    ssn character(9) NOT NULL,
    "position" character varying(100),
    feedback text
);


ALTER TABLE "IMPACT Club".mentor OWNER TO postgres;

--
-- Name: mentor_mentorid_seq; Type: SEQUENCE; Schema: IMPACT Club; Owner: postgres
--

CREATE SEQUENCE "IMPACT Club".mentor_mentorid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE "IMPACT Club".mentor_mentorid_seq OWNER TO postgres;

--
-- Name: mentor_mentorid_seq; Type: SEQUENCE OWNED BY; Schema: IMPACT Club; Owner: postgres
--

ALTER SEQUENCE "IMPACT Club".mentor_mentorid_seq OWNED BY "IMPACT Club".mentor.mentorid;


--
-- Name: monthlyplan; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".monthlyplan (
    monthlyplan_id integer NOT NULL,
    yearplan_id integer,
    month integer,
    status boolean DEFAULT false,
    CONSTRAINT monthlyplan_month_check CHECK (((month >= 1) AND (month <= 12)))
);


ALTER TABLE "IMPACT Club".monthlyplan OWNER TO postgres;

--
-- Name: monthlyplan_monthlyplan_id_seq; Type: SEQUENCE; Schema: IMPACT Club; Owner: postgres
--

CREATE SEQUENCE "IMPACT Club".monthlyplan_monthlyplan_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE "IMPACT Club".monthlyplan_monthlyplan_id_seq OWNER TO postgres;

--
-- Name: monthlyplan_monthlyplan_id_seq; Type: SEQUENCE OWNED BY; Schema: IMPACT Club; Owner: postgres
--

ALTER SEQUENCE "IMPACT Club".monthlyplan_monthlyplan_id_seq OWNED BY "IMPACT Club".monthlyplan.monthlyplan_id;


--
-- Name: person; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".person (
    ssn character(9) NOT NULL,
    first_name character varying(30) NOT NULL,
    middle_name character varying(30),
    last_name character varying(30) NOT NULL,
    gender character(1),
    bod date,
    phone_number character varying(15),
    user_name character varying(30) NOT NULL,
    password character varying(30) NOT NULL,
    street character varying(50),
    city character varying(20),
    start_date date,
    CONSTRAINT person_gender_check CHECK ((gender = ANY (ARRAY['M'::bpchar, 'F'::bpchar])))
);


ALTER TABLE "IMPACT Club".person OWNER TO postgres;

--
-- Name: plan; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".plan (
    planid integer NOT NULL,
    startdate date NOT NULL,
    enddate date NOT NULL,
    status boolean DEFAULT false
);


ALTER TABLE "IMPACT Club".plan OWNER TO postgres;

--
-- Name: plan_planid_seq; Type: SEQUENCE; Schema: IMPACT Club; Owner: postgres
--

CREATE SEQUENCE "IMPACT Club".plan_planid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE "IMPACT Club".plan_planid_seq OWNER TO postgres;

--
-- Name: plan_planid_seq; Type: SEQUENCE OWNED BY; Schema: IMPACT Club; Owner: postgres
--

ALTER SEQUENCE "IMPACT Club".plan_planid_seq OWNED BY "IMPACT Club".plan.planid;


--
-- Name: project; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".project (
    projectid integer NOT NULL,
    projectname character varying(100) NOT NULL,
    description text,
    projectstatus "IMPACT Club".projectstatustype DEFAULT 'In Progress'::"IMPACT Club".projectstatustype,
    objectives text
);


ALTER TABLE "IMPACT Club".project OWNER TO postgres;

--
-- Name: projectleader; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".projectleader (
    projectid integer NOT NULL,
    leaderid integer NOT NULL,
    projectfeedback text,
    role character varying(200)
);


ALTER TABLE "IMPACT Club".projectleader OWNER TO postgres;

--
-- Name: projectplan; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".projectplan (
    planid integer NOT NULL,
    projectid integer NOT NULL,
    budget numeric(10,2),
    expenditure numeric(10,2),
    fundingsources text,
    objectives text
);


ALTER TABLE "IMPACT Club".projectplan OWNER TO postgres;

--
-- Name: session; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".session (
    sessionid integer NOT NULL,
    sessiondate date NOT NULL,
    topic character varying(100),
    duration time without time zone
);


ALTER TABLE "IMPACT Club".session OWNER TO postgres;

--
-- Name: sessionleader; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".sessionleader (
    sessionid integer NOT NULL,
    leaderid integer NOT NULL,
    role character varying(200),
    leadernotes text,
    attendance "IMPACT Club".isattendence
);


ALTER TABLE "IMPACT Club".sessionleader OWNER TO postgres;

--
-- Name: sessionmember; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".sessionmember (
    sessionid integer NOT NULL,
    memberid integer NOT NULL,
    attendance "IMPACT Club".isattendence,
    answers text,
    feedback text
);


ALTER TABLE "IMPACT Club".sessionmember OWNER TO postgres;

--
-- Name: sessionmentor; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".sessionmentor (
    sessionid integer NOT NULL,
    mentorid integer NOT NULL,
    guidancenotes text,
    attendance "IMPACT Club".isattendence
);


ALTER TABLE "IMPACT Club".sessionmentor OWNER TO postgres;

--
-- Name: tasks; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".tasks (
    task_id integer NOT NULL,
    monthlyplan_id integer,
    task_name character varying(200) NOT NULL,
    completed boolean DEFAULT false
);


ALTER TABLE "IMPACT Club".tasks OWNER TO postgres;

--
-- Name: tasks_task_id_seq; Type: SEQUENCE; Schema: IMPACT Club; Owner: postgres
--

CREATE SEQUENCE "IMPACT Club".tasks_task_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE "IMPACT Club".tasks_task_id_seq OWNER TO postgres;

--
-- Name: tasks_task_id_seq; Type: SEQUENCE OWNED BY; Schema: IMPACT Club; Owner: postgres
--

ALTER SEQUENCE "IMPACT Club".tasks_task_id_seq OWNED BY "IMPACT Club".tasks.task_id;


--
-- Name: yearlyplan; Type: TABLE; Schema: IMPACT Club; Owner: postgres
--

CREATE TABLE "IMPACT Club".yearlyplan (
    yearlyplanid integer NOT NULL,
    planid integer NOT NULL,
    numberofprojects integer DEFAULT 0,
    numberofsessions integer DEFAULT 0,
    numberofrewards integer DEFAULT 0
);


ALTER TABLE "IMPACT Club".yearlyplan OWNER TO postgres;

--
-- Name: yearlyplan_yearlyplanid_seq; Type: SEQUENCE; Schema: IMPACT Club; Owner: postgres
--

CREATE SEQUENCE "IMPACT Club".yearlyplan_yearlyplanid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE "IMPACT Club".yearlyplan_yearlyplanid_seq OWNER TO postgres;

--
-- Name: yearlyplan_yearlyplanid_seq; Type: SEQUENCE OWNED BY; Schema: IMPACT Club; Owner: postgres
--

ALTER SEQUENCE "IMPACT Club".yearlyplan_yearlyplanid_seq OWNED BY "IMPACT Club".yearlyplan.yearlyplanid;


--
-- Name: leader leaderid; Type: DEFAULT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".leader ALTER COLUMN leaderid SET DEFAULT nextval('"IMPACT Club".leader_leaderid_seq'::regclass);


--
-- Name: member memberid; Type: DEFAULT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".member ALTER COLUMN memberid SET DEFAULT nextval('"IMPACT Club".member_memberid_seq'::regclass);


--
-- Name: mentor mentorid; Type: DEFAULT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".mentor ALTER COLUMN mentorid SET DEFAULT nextval('"IMPACT Club".mentor_mentorid_seq'::regclass);


--
-- Name: monthlyplan monthlyplan_id; Type: DEFAULT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".monthlyplan ALTER COLUMN monthlyplan_id SET DEFAULT nextval('"IMPACT Club".monthlyplan_monthlyplan_id_seq'::regclass);


--
-- Name: plan planid; Type: DEFAULT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".plan ALTER COLUMN planid SET DEFAULT nextval('"IMPACT Club".plan_planid_seq'::regclass);


--
-- Name: tasks task_id; Type: DEFAULT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".tasks ALTER COLUMN task_id SET DEFAULT nextval('"IMPACT Club".tasks_task_id_seq'::regclass);


--
-- Name: yearlyplan yearlyplanid; Type: DEFAULT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".yearlyplan ALTER COLUMN yearlyplanid SET DEFAULT nextval('"IMPACT Club".yearlyplan_yearlyplanid_seq'::regclass);


--
-- Data for Name: leader; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".leader (leaderid, ssn, major) FROM stdin;
1	410741852	Psychology
2	420213605	Doctor of Pharmacy
3	420741852	Computer Engineering 
4	421579285	Digital Marketing 
\.


--
-- Data for Name: leader_course; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".leader_course (leaderid, courseid, topic, goals, date, duration, feedback) FROM stdin;
1	101	Psychological Release and Support through Art and Music	To learn techniques for emotional release and support through music and art.	2024-10-01	10	The course was enlightening and provided practical techniques for emotional support.
1	102	Initiative Writing and Community Contribution	Learn criteria for selecting initiatives and ensuring community contributions.	2024-10-15	6 hours	Gained insights on initiative selection and community impact.
2	102	Initiative Writing and Community Contribution	Learn criteria for selecting initiatives and ensuring community contributions.	2024-10-15	6 hours	The course provided useful guidelines for meaningful initiatives.
3	102	Initiative Writing and Community Contribution	Learn criteria for selecting initiatives and ensuring community contributions.	2024-10-15	6 hours	Helped clarify how to align initiatives with community needs.
4	102	Initiative Writing and Community Contribution	Learn criteria for selecting initiatives and ensuring community contributions.	2024-10-15	6 hours	Invaluable in understanding community-centered initiative planning.
\.


--
-- Data for Name: member; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".member (memberid, ssn, personaldevelopmentgoals, skillsdeveloped, favoriteactivities, personal_test) FROM stdin;
1	422123456	Enhance public speaking skills	Leadership, Time Management	Team sports, Debate club	ENFP
2	422223456	Improve time management	Organization, Prioritization	Reading, Workshops	ISFJ
3	422323456	Enhance teamwork skills	Collaboration, Conflict Resolution	Outdoor activities, Group projects	ENTP
4	422423456	Develop critical thinking	Analytical skills, Decision making	Debate clubs, Reading discussions	INTJ
5	422523456	Learn negotiation skills	Persuasion, Conflict Management	Role-playing games, Group discussions	ESTJ
\.


--
-- Data for Name: memberproject; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".memberproject (memberid, projectid, roleinproject, projectinvolvementlevel, projectfeedback, skillsdevelopedinproject) FROM stdin;
1	1	Volunteer	85	Active and engaged throughout the project. Provided ideas and worked hard on the ground.	Leadership, teamwork, project planning
3	1	Event Coordinator	75	Helped in organizing events and coordinating with local authorities. Highly cooperative and proactive.Event coordination, communication, community engagement	\N
5	2	Library Organizer	95	Responsible for sorting and organizing books effectively. Helped create an inviting space for readers.	\N
2	3	Photography Assistant	85	Assisted participants in taking photos during the tour and managed the social media updates.	\N
4	2	Book Cataloger	78	Successfully cataloged all new arrivals and assisted in creating an efficient tracking system.	\N
\.


--
-- Data for Name: mentor; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".mentor (mentorid, ssn, "position", feedback) FROM stdin;
1	422623456	Responsible for the Impact Club and Makani Club  	\N
\.


--
-- Data for Name: monthlyplan; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".monthlyplan (monthlyplan_id, yearplan_id, month, status) FROM stdin;
1	2024	1	f
2	2024	2	t
3	2024	3	f
4	2024	4	t
5	2024	5	f
6	2024	6	t
7	2024	7	f
8	2024	8	t
9	2024	9	f
10	2024	10	t
11	2024	11	f
12	2024	12	t
\.


--
-- Data for Name: person; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".person (ssn, first_name, middle_name, last_name, gender, bod, phone_number, user_name, password, street, city, start_date) FROM stdin;
421579285	Jehad	Amjad	Jafar	F	2004-06-17	568900152	@JehadJafar	123456Jehad	mainstreet/Naqoura	Nablus	2023-07-05
420213605	Zinab	Nather	Saad	F	2004-08-24	594017422	@ZinabSaad	123456Zinab	thiraa street/Naqoura	Nablus	2023-07-05
420741852	Afnan	Firas	Hasan	F	2005-01-05	594017422	@AfnanHasan	123456Afnan	thiraa street/Naqoura	Nablus	2023-07-05
410741852	Reham	Sameeh	Mohsen	F	1988-05-07	599396375	@RehamMohsen	123456Reham	thiraa street/Naqoura	Nablus	2023-07-05
422123456	Miar	Hilal	Abdoon	F	2010-02-25	5900000	@MiarAbdoon	123456Miar	AinStreet/Naqoora	Nablus	2023-07-05
422223456	Lamar	Firas	hasheesh	F	2010-05-25	5900000	@LamarHasheesh	123456Lamar	MainStreet/Naqoora	Nablus	2023-07-05
422323456	Lama	Naser	Zreaq	F	2012-12-12	59000000	@LamaZreaq	123456Lama	MoutineStreet/Naqoora	Nablus	2023-07-05
422423456	Lama	Samer	Mohsen	F	2011-03-02	59000000	@LamaMohsen	123456Lama	MainStreet/Naqoora	Nablus	2023-07-05
422523456	Najdoleen	Waleed	Watood	F	2009-03-02	59000000	@NajdoleenWatood	123456Watood	KhalaStreet/Naqoora	Nablus	2023-07-05
422623456	Adeen	Waleed	Thafer	F	1991-05-30	593116772	@AdeenThafer	123456Adeen	MainStreet/Rameen	Tulkarem	2023-12-01
\.


--
-- Data for Name: plan; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".plan (planid, startdate, enddate, status) FROM stdin;
111	2024-03-05	2024-04-20	f
112	2024-06-01	2024-12-01	f
113	2024-07-14	2024-07-15	f
114	2024-08-10	2024-08-15	f
115	2024-09-09	2024-09-09	f
6	2024-01-01	2024-12-31	f
\.


--
-- Data for Name: project; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".project (projectid, projectname, description, projectstatus, objectives) FROM stdin;
1	Playground Rehabilitation	Creating a safe play area for children and a space for community activities.	Completed	Provide a safe space for children to play and hold future community activities.
2	Library Establishment	Creating a public library for community access to educational resources.	In Progress	Provide a space for community learning and resource access.
3	Impact Tours	Photography training for members and exploration of heritage sites.	Completed	Develop members’ photography skills and foster appreciation of heritage sites.
4	Children Summer Camp	A 5-day summer camp for children focusing on fun, learning, and development activities.	Completed	Provide a safe and engaging environment for children to learn and have fun.
5	Children Fun Day	A one-day fun event for children aged 4-10, including games, storytelling, and creative activities.	Completed	Provide a day of joy and learning through fun activities for young children.
\.


--
-- Data for Name: projectleader; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".projectleader (projectid, leaderid, projectfeedback, role) FROM stdin;
4	4	Activity Planner	Organized all activities and ensured the safety of participants throughout the camp.
3	3	Tour Guide	Provided an engaging and informative experience for all participants.
2	2	Library Development Coordinator	Oversaw the entire library setup process, ensuring the organization of books and resources to facilitate learning.
1	1	Project Supervisor	Managed the rehabilitation process, ensuring the space was safe and ready for children to play and engage in activities.
1	4	Safety Officer	Ensured all safety protocols were followed during the rehabilitation process and monitored the site for hazards.
\.


--
-- Data for Name: projectplan; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".projectplan (planid, projectid, budget, expenditure, fundingsources, objectives) FROM stdin;
111	1	20000.00	3000.00	World Vision Foundation, Local Council	Rehabilitate the space for community use and children’s activities.
112	2	15000.00	5000.00	Community Donations, World Vision	Establish a library with books, digital resources, and study spaces.
113	3	500.00	500.00	Community Support, World Vision	Provide hands-on photography training and heritage site exploration.
114	4	2000.00	500.00	Local Sponsorship, Community Donations	Provide various activities such as sports, arts, and educational workshops.
115	5	500.00	50.00	World Vision	Organize various games, storytelling sessions, and crafts for children aged 4-10.
\.


--
-- Data for Name: session; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".session (sessionid, sessiondate, topic, duration) FROM stdin;
5	2024-05-29	Effective Leadership Skills	01:45:00
3	2024-05-15	Enhancing Teamwork	02:00:00
2	2024-05-08	Strategic Planning for Projects	01:30:00
1	2024-05-01	Personal Development Skills	02:00:00
4	2024-05-22	Using Social Media	02:30:00
\.


--
-- Data for Name: sessionleader; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".sessionleader (sessionid, leaderid, role, leadernotes, attendance) FROM stdin;
1	1	Guided discussions and ensured everyone participated.	 The session went smoothly and effectively.	Present
2	2	Assisted in group activities and monitored progress.	Engaged well with the members.	Present
3	3	Delivered a presentation on strategic planning.	Great interaction with the audience.	Present
4	4	Managed the logistics of the session.	Everything was organized perfectly.	Present
5	1	Provided insights and feedback during the session.	The members were responsive and engaged.	\N
\.


--
-- Data for Name: sessionmember; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".sessionmember (sessionid, memberid, attendance, answers, feedback) FROM stdin;
1	1	Present	Completed the personality test and engaged actively in discussions.	Great session, learned a lot about personal development.
2	2	Absent		Missed the session due to personal reasons.
3	3	Present	Participated in team activities and shared ideas.	Enjoyed the teamwork exercises.
4	4	Present	Provided answers to all session questions.	Found the session informative and helpful.
5	5	Present	Engaged in discussions and provided feedback.	The session was inspiring and motivating.
\.


--
-- Data for Name: sessionmentor; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".sessionmentor (sessionid, mentorid, guidancenotes, attendance) FROM stdin;
1	1	Provided individual guidance to members on personal growth.	Present
2	1	Guided discussions and offered support on project planning.	Present
3	1	Shared expertise on teamwork and collaboration.	Present
4	1	Advised members on effective communication strategies.	Absent
5	1	Provided feedback on group activities and presentations.	Present
\.


--
-- Data for Name: tasks; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".tasks (task_id, monthlyplan_id, task_name, completed) FROM stdin;
3	2	Set up February fundraising event	f
4	2	Finalize speakers for February conference	t
7	4	Develop April team-building activities	f
8	4	Coordinate with April event partners	t
9	5	Design May project brochures	f
10	5	Prepare budget for May project	t
11	6	Plan June skill development workshops	f
12	6	Recruit trainers for June sessions	t
5	3	March community outreach planning	t
6	3	Create marketing plan for March	t
14	1	have a test	f
13	1	have a session	f
1	1	Organize January workshop	t
2	1	Prepare materials for January session	t
15	1	prepare for the project	t
16	1	do a trip	t
\.


--
-- Data for Name: yearlyplan; Type: TABLE DATA; Schema: IMPACT Club; Owner: postgres
--

COPY "IMPACT Club".yearlyplan (yearlyplanid, planid, numberofprojects, numberofsessions, numberofrewards) FROM stdin;
2024	6	4	60	4
\.


--
-- Name: leader_leaderid_seq; Type: SEQUENCE SET; Schema: IMPACT Club; Owner: postgres
--

SELECT pg_catalog.setval('"IMPACT Club".leader_leaderid_seq', 1, false);


--
-- Name: member_memberid_seq; Type: SEQUENCE SET; Schema: IMPACT Club; Owner: postgres
--

SELECT pg_catalog.setval('"IMPACT Club".member_memberid_seq', 1, false);


--
-- Name: mentor_mentorid_seq; Type: SEQUENCE SET; Schema: IMPACT Club; Owner: postgres
--

SELECT pg_catalog.setval('"IMPACT Club".mentor_mentorid_seq', 1, false);


--
-- Name: monthlyplan_monthlyplan_id_seq; Type: SEQUENCE SET; Schema: IMPACT Club; Owner: postgres
--

SELECT pg_catalog.setval('"IMPACT Club".monthlyplan_monthlyplan_id_seq', 12, true);


--
-- Name: plan_planid_seq; Type: SEQUENCE SET; Schema: IMPACT Club; Owner: postgres
--

SELECT pg_catalog.setval('"IMPACT Club".plan_planid_seq', 1, false);


--
-- Name: tasks_task_id_seq; Type: SEQUENCE SET; Schema: IMPACT Club; Owner: postgres
--

SELECT pg_catalog.setval('"IMPACT Club".tasks_task_id_seq', 16, true);


--
-- Name: yearlyplan_yearlyplanid_seq; Type: SEQUENCE SET; Schema: IMPACT Club; Owner: postgres
--

SELECT pg_catalog.setval('"IMPACT Club".yearlyplan_yearlyplanid_seq', 1, false);


--
-- Name: leader_course leader_course_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".leader_course
    ADD CONSTRAINT leader_course_pkey PRIMARY KEY (leaderid, courseid);


--
-- Name: leader leader_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".leader
    ADD CONSTRAINT leader_pkey PRIMARY KEY (leaderid);


--
-- Name: member member_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".member
    ADD CONSTRAINT member_pkey PRIMARY KEY (memberid);


--
-- Name: memberproject memberproject_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".memberproject
    ADD CONSTRAINT memberproject_pkey PRIMARY KEY (memberid, projectid);


--
-- Name: mentor mentor_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".mentor
    ADD CONSTRAINT mentor_pkey PRIMARY KEY (mentorid);


--
-- Name: monthlyplan monthlyplan_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".monthlyplan
    ADD CONSTRAINT monthlyplan_pkey PRIMARY KEY (monthlyplan_id);


--
-- Name: person person_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".person
    ADD CONSTRAINT person_pkey PRIMARY KEY (ssn);


--
-- Name: person person_user_name_key; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".person
    ADD CONSTRAINT person_user_name_key UNIQUE (user_name);


--
-- Name: plan plan_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".plan
    ADD CONSTRAINT plan_pkey PRIMARY KEY (planid);


--
-- Name: project project_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".project
    ADD CONSTRAINT project_pkey PRIMARY KEY (projectid);


--
-- Name: projectleader projectleader_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".projectleader
    ADD CONSTRAINT projectleader_pkey PRIMARY KEY (projectid, leaderid);


--
-- Name: projectplan projectplan_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".projectplan
    ADD CONSTRAINT projectplan_pkey PRIMARY KEY (planid, projectid);


--
-- Name: projectplan projectplan_planid_key; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".projectplan
    ADD CONSTRAINT projectplan_planid_key UNIQUE (planid);


--
-- Name: projectplan projectplan_projectid_key; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".projectplan
    ADD CONSTRAINT projectplan_projectid_key UNIQUE (projectid);


--
-- Name: session session_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".session
    ADD CONSTRAINT session_pkey PRIMARY KEY (sessionid);


--
-- Name: sessionleader sessionleader_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".sessionleader
    ADD CONSTRAINT sessionleader_pkey PRIMARY KEY (sessionid, leaderid);


--
-- Name: sessionmember sessionmember_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".sessionmember
    ADD CONSTRAINT sessionmember_pkey PRIMARY KEY (sessionid, memberid);


--
-- Name: sessionmentor sessionmentor_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".sessionmentor
    ADD CONSTRAINT sessionmentor_pkey PRIMARY KEY (sessionid, mentorid);


--
-- Name: tasks tasks_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (task_id);


--
-- Name: yearlyplan yearlyplan_pkey; Type: CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".yearlyplan
    ADD CONSTRAINT yearlyplan_pkey PRIMARY KEY (yearlyplanid);


--
-- Name: leader_course leader_course_leaderid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".leader_course
    ADD CONSTRAINT leader_course_leaderid_fkey FOREIGN KEY (leaderid) REFERENCES "IMPACT Club".leader(leaderid);


--
-- Name: leader leader_ssn_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".leader
    ADD CONSTRAINT leader_ssn_fkey FOREIGN KEY (ssn) REFERENCES "IMPACT Club".person(ssn);


--
-- Name: member member_ssn_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".member
    ADD CONSTRAINT member_ssn_fkey FOREIGN KEY (ssn) REFERENCES "IMPACT Club".person(ssn);


--
-- Name: memberproject memberproject_memberid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".memberproject
    ADD CONSTRAINT memberproject_memberid_fkey FOREIGN KEY (memberid) REFERENCES "IMPACT Club".member(memberid);


--
-- Name: memberproject memberproject_projectid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".memberproject
    ADD CONSTRAINT memberproject_projectid_fkey FOREIGN KEY (projectid) REFERENCES "IMPACT Club".project(projectid);


--
-- Name: mentor mentor_ssn_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".mentor
    ADD CONSTRAINT mentor_ssn_fkey FOREIGN KEY (ssn) REFERENCES "IMPACT Club".person(ssn);


--
-- Name: monthlyplan monthlyplan_yearplan_id_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".monthlyplan
    ADD CONSTRAINT monthlyplan_yearplan_id_fkey FOREIGN KEY (yearplan_id) REFERENCES "IMPACT Club".yearlyplan(yearlyplanid);


--
-- Name: projectleader projectleader_leaderid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".projectleader
    ADD CONSTRAINT projectleader_leaderid_fkey FOREIGN KEY (leaderid) REFERENCES "IMPACT Club".leader(leaderid);


--
-- Name: projectleader projectleader_projectid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".projectleader
    ADD CONSTRAINT projectleader_projectid_fkey FOREIGN KEY (projectid) REFERENCES "IMPACT Club".project(projectid);


--
-- Name: projectplan projectplan_planid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".projectplan
    ADD CONSTRAINT projectplan_planid_fkey FOREIGN KEY (planid) REFERENCES "IMPACT Club".plan(planid);


--
-- Name: projectplan projectplan_projectid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".projectplan
    ADD CONSTRAINT projectplan_projectid_fkey FOREIGN KEY (projectid) REFERENCES "IMPACT Club".project(projectid);


--
-- Name: sessionleader sessionleader_leaderid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".sessionleader
    ADD CONSTRAINT sessionleader_leaderid_fkey FOREIGN KEY (leaderid) REFERENCES "IMPACT Club".leader(leaderid);


--
-- Name: sessionleader sessionleader_sessionid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".sessionleader
    ADD CONSTRAINT sessionleader_sessionid_fkey FOREIGN KEY (sessionid) REFERENCES "IMPACT Club".session(sessionid);


--
-- Name: sessionmember sessionmember_memberid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".sessionmember
    ADD CONSTRAINT sessionmember_memberid_fkey FOREIGN KEY (memberid) REFERENCES "IMPACT Club".member(memberid);


--
-- Name: sessionmember sessionmember_sessionid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".sessionmember
    ADD CONSTRAINT sessionmember_sessionid_fkey FOREIGN KEY (sessionid) REFERENCES "IMPACT Club".session(sessionid);


--
-- Name: sessionmentor sessionmentor_mentorid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".sessionmentor
    ADD CONSTRAINT sessionmentor_mentorid_fkey FOREIGN KEY (mentorid) REFERENCES "IMPACT Club".mentor(mentorid);


--
-- Name: sessionmentor sessionmentor_sessionid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".sessionmentor
    ADD CONSTRAINT sessionmentor_sessionid_fkey FOREIGN KEY (sessionid) REFERENCES "IMPACT Club".session(sessionid);


--
-- Name: tasks tasks_monthlyplan_id_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".tasks
    ADD CONSTRAINT tasks_monthlyplan_id_fkey FOREIGN KEY (monthlyplan_id) REFERENCES "IMPACT Club".monthlyplan(monthlyplan_id) ON DELETE CASCADE;


--
-- Name: yearlyplan yearlyplan_planid_fkey; Type: FK CONSTRAINT; Schema: IMPACT Club; Owner: postgres
--

ALTER TABLE ONLY "IMPACT Club".yearlyplan
    ADD CONSTRAINT yearlyplan_planid_fkey FOREIGN KEY (planid) REFERENCES "IMPACT Club".plan(planid);


--
-- PostgreSQL database dump complete
--

