package com.example.qbankapi.config;

import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.entity.Question;
import com.example.qbankapi.service.AppInitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.time.*;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppInitConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final AppInitService initService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

//        Subject
        log.info("Creating default subjects if not exists");

        initService.createSubjectIfNotExists(
                "Data Structures",
                "Organizing and storing data for efficient access and modification."
        );

        initService.createSubjectIfNotExists(
                "Algorithms",
                "Step-by-step procedures for solving computational problems."
        );

        initService.createSubjectIfNotExists(
                "Database Management Systems",
                "Designing and managing structured data using relational models."
        );

        initService.createSubjectIfNotExists(
                "Operating Systems",
                "Managing hardware and software resources in computing environments."
        );

        initService.createSubjectIfNotExists(
                "Computer Networks",
                "Transmission of data and communication between connected systems."
        );

        initService.createSubjectIfNotExists(
                "Web Development",
                "Building and maintaining websites and web applications."
        );



//        BaseUser
        log.info("Creating default base users if not exists");

//        Admin
        log.info("Creating default admin if not exists");
        initService.createAndSaveAdminIfNotExists(
                "admin",
                "admin@school.com",
                "Admin@100",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(100)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

//        Teacher
        log.info("Creating default teacher if not exists");
        initService.createAndSaveTeacherIfNotExists(
                "rahul.sharma",
                "rahul.sharma.teacher@school.com",
                "Teach@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(40)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveTeacherIfNotExists(
                "priya.verma",
                "priya.verma.teacher@school.com",
                "Teach@102",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(50)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveTeacherIfNotExists(
                "arjun.patel",
                "arjun.patel.teacher@school.com",
                "Teach@103",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(45)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveTeacherIfNotExists(
                "neha.jain",
                "neha.jain.teacher@school.com",
                "Teach@104",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(41)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveTeacherIfNotExists(
                "vishal.rana",
                "vishal.rana.teacher@school.com",
                "Teach@105",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(55)),
                BaseUser.Status.INACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveTeacherIfNotExists(
                "anita.mehra",
                "anita.mehra.teacher@school.com",
                "Teach@106",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(57)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveTeacherIfNotExists(
                "amit.dubey",
                "amit.dubey.teacher@school.com",
                "Teach@107",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(48)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveTeacherIfNotExists(
                "kiran.kapoor",
                "kiran.kapoor.teacher@school.com",
                "Teach@108",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(56)),
                BaseUser.Status.INACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

//        User
        log.info("Creating default user if not exists");
        initService.createAndSaveUserIfNotExists(
                "sahil.kanojiya",
                "sahil.kanojiya.student@school.com",
                "sahil@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(20)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveUserIfNotExists(
                "rohit.kumar",
                "rohit.kumar.student@school.com",
                "rohit@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(25)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveUserIfNotExists(
                "sneha.rathi",
                "sneha.rathi.student@school.com",
                "sneha@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(27)),
                BaseUser.Status.INACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveUserIfNotExists(
                "deepak.nair",
                "deepak.nair.student@school.com",
                "deepak@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(17)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveUserIfNotExists(
                "ritu.sen",
                "ritu.sen.student@school.com",
                "ritu@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(19)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveUserIfNotExists(
                "manish.goel",
                "manish.goel.student@school.com",
                "manish@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(30)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveUserIfNotExists(
                "divya.modi",
                "divya.modi.student@school.com",
                "divya@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(28)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveUserIfNotExists(
                "sachin.singh",
                "sachin.singh.student@school.com",
                "sachin@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(23)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveUserIfNotExists(
                "pooja.bhatt",
                "pooja.bhatt.student@school.com",
                "pooja@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(24)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveUserIfNotExists(
                "alok.joshi",
                "alok.joshi.student@school.com",
                "alok@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(21)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveUserIfNotExists(
                "meena.yadav",
                "meena.yadav.student@school.com",
                "meena@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(22)),
                BaseUser.Status.ACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );

        initService.createAndSaveUserIfNotExists(
                "nikhil.raut",
                "nikhil.raut.student@school.com",
                "nikhil@101",
                ZonedDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(14)),
                BaseUser.Status.INACTIVE,
                TimeZoneId.ASIA_KOLKATA.getZoneId()
        );



        log.info("Creating default questions if not exists");

//        Subject 1
        initService.createQuestionIfNotExists(
                "<p><strong>What is the time complexity of accessing an element in an array?</strong></p>",
                List.of("<p>O(1)</p>", "<p>O(n)</p>", "<p>O(log n)</p>", "<p>O(n log n)</p>"),
                Question.Option.A,
                Question.Complexity.EASY,
                1L,
                1L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which data structure uses FIFO principle?</strong></p>",
                List.of("<p>Stack</p>", "<p>Queue</p>", "<p>Tree</p>", "<p>Graph</p>"),
                Question.Option.B,
                Question.Complexity.EASY,
                1L,
                1L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which data structure uses LIFO principle?</strong></p>",
                List.of("<p>Queue</p>", "<p>Linked List</p>", "<p>Stack</p>", "<p>Array</p>"),
                Question.Option.C,
                Question.Complexity.EASY,
                2L,
                1L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which traversal algorithm uses a queue?</strong></p>",
                List.of("<p>Inorder</p>", "<p>Postorder</p>", "<p>BFS</p>", "<p>DFS</p>"),
                Question.Option.C,
                Question.Complexity.EASY,
                2L,
                1L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which data structure is best for recursive function calls?</strong></p>",
                List.of("<p>Queue</p>", "<p>Array</p>", "<p>Stack</p>", "<p>Graph</p>"),
                Question.Option.C,
                Question.Complexity.MEDIUM,
                3L,
                1L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which structure is used for implementing LRU Cache?</strong></p>",
                List.of("<p>HashMap + Queue</p>", "<p>HashMap + LinkedList</p>", "<p>Array</p>", "<p>Heap</p>"),
                Question.Option.B,
                Question.Complexity.MEDIUM,
                3L,
                1L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>In a complete binary tree, how many children can a node have?</strong></p>",
                List.of("<p>0</p>", "<p>1</p>", "<p>2</p>", "<p>0, 1 or 2</p>"),
                Question.Option.D,
                Question.Complexity.MEDIUM,
                4L,
                1L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>What is the height of a balanced binary tree with n nodes?</strong></p>",
                List.of("<p>O(n)</p>", "<p>O(log n)</p>", "<p>O(n log n)</p>", "<p>O(1)</p>"),
                Question.Option.B,
                Question.Complexity.MEDIUM,
                4L,
                1L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which operation is fastest in a heap?</strong></p>",
                List.of("<p>Insert</p>", "<p>Search</p>", "<p>Delete Max</p>", "<p>Find Max</p>"),
                Question.Option.D,
                Question.Complexity.HARD,
                5L,
                1L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>What data structure is used in a call stack?</strong></p>",
                List.of("<p>Queue</p>", "<p>Stack</p>", "<p>Deque</p>", "<p>Set</p>"),
                Question.Option.B,
                Question.Complexity.HARD,
                5L,
                1L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which is the best data structure for implementing a priority queue?</strong></p>",
                List.of("<p>Stack</p>", "<p>Queue</p>", "<p>Heap</p>", "<p>Tree</p>"),
                Question.Option.C,
                Question.Complexity.HARD,
                6L,
                1L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which traversal visits nodes in ascending order in a BST?</strong></p>",
                List.of("<p>Preorder</p>", "<p>Postorder</p>", "<p>Inorder</p>", "<p>BFS</p>"),
                Question.Option.C,
                Question.Complexity.HARD,
                6L,
                1L
        );

//        Subject 2
        initService.createQuestionIfNotExists(
                "<p><strong>What is the time complexity of binary search?</strong></p>",
                List.of("<p>O(1)</p>", "<p>O(n)</p>", "<p>O(log n)</p>", "<p>O(n log n)</p>"),
                Question.Option.C,
                Question.Complexity.EASY,
                1L,
                2L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which algorithm uses divide and conquer?</strong></p>",
                List.of("<p>Merge Sort</p>", "<p>Bubble Sort</p>", "<p>Linear Search</p>", "<p>Insertion Sort</p>"),
                Question.Option.A,
                Question.Complexity.EASY,
                1L,
                2L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>What is the worst case of QuickSort?</strong></p>",
                List.of("<p>O(n)</p>", "<p>O(n log n)</p>", "<p>O(n^2)</p>", "<p>O(log n)</p>"),
                Question.Option.C,
                Question.Complexity.EASY,
                2L,
                2L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which algorithm is used for shortest path in graphs?</strong></p>",
                List.of("<p>Dijkstra</p>", "<p>DFS</p>", "<p>BFS</p>", "<p>Prim's</p>"),
                Question.Option.A,
                Question.Complexity.EASY,
                2L,
                2L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which sort is stable and non-comparison based?</strong></p>",
                List.of("<p>Merge Sort</p>", "<p>Heap Sort</p>", "<p>Counting Sort</p>", "<p>Quick Sort</p>"),
                Question.Option.C,
                Question.Complexity.MEDIUM,
                3L,
                2L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which algorithm finds MST?</strong></p>",
                List.of("<p>Dijkstra</p>", "<p>Floyd-Warshall</p>", "<p>Prim's</p>", "<p>Bellman-Ford</p>"),
                Question.Option.C,
                Question.Complexity.MEDIUM,
                3L,
                2L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>What is Kadane’s Algorithm used for?</strong></p>",
                List.of("<p>Longest Substring</p>", "<p>Sorting</p>", "<p>Max Subarray Sum</p>", "<p>Graph Traversal</p>"),
                Question.Option.C,
                Question.Complexity.MEDIUM,
                4L,
                2L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which sorting algorithm is in-place?</strong></p>",
                List.of("<p>Merge Sort</p>", "<p>Quick Sort</p>", "<p>Counting Sort</p>", "<p>Bucket Sort</p>"),
                Question.Option.B,
                Question.Complexity.MEDIUM,
                4L,
                2L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which algorithm uses greedy strategy?</strong></p>",
                List.of("<p>Dijkstra</p>", "<p>DFS</p>", "<p>Backtracking</p>", "<p>Quick Sort</p>"),
                Question.Option.A,
                Question.Complexity.HARD,
                5L,
                2L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which technique is used in DP?</strong></p>",
                List.of("<p>Memoization</p>", "<p>Divide & Conquer</p>", "<p>Greedy</p>", "<p>All of the above</p>"),
                Question.Option.D,
                Question.Complexity.HARD,
                5L,
                2L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which algorithm is commonly used to detect cycles in a graph?</strong></p>",
                List.of("<p>Dijkstra’s Algorithm</p>", "<p>Bellman-Ford Algorithm</p>", "<p>Floyd-Warshall Algorithm</p>", "<p>Union-Find</p>"),
                Question.Option.D,
                Question.Complexity.HARD,
                6L,
                2L
        );

        initService.createQuestionIfNotExists(
                "<p><strong>Which algorithm solves the All-Pairs Shortest Path problem?</strong></p>",
                List.of("<p>Bellman-Ford</p>", "<p>Prim’s Algorithm</p>", "<p>Floyd-Warshall</p>", "<p>Kruskal’s Algorithm</p>"),
                Question.Option.C,
                Question.Complexity.HARD,
                6L,
                2L
        );

    }

    public enum TimeZoneId {

        UTC("UTC"),
        AMERICA_NEW_YORK("America/New_York"),
        AMERICA_LOS_ANGELES("America/Los_Angeles"),
        EUROPE_LONDON("Europe/London"),
        EUROPE_BERLIN("Europe/Berlin"),
        ASIA_KOLKATA("Asia/Kolkata"),
        ASIA_TOKYO("Asia/Tokyo"),
        AUSTRALIA_SYDNEY("Australia/Sydney"),
        AFRICA_JOHANNESBURG("Africa/Johannesburg");

        private final String zoneId;

        TimeZoneId(String zoneId) {
            this.zoneId = zoneId;
        }

        public String getZoneId() {
            return zoneId;
        }

        public ZoneId toZoneId() {
            return ZoneId.of(zoneId);
        }

        @Override
        public String toString() {
            return zoneId;
        }
    }

}
