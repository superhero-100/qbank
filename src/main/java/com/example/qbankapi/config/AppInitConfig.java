package com.example.qbankapi.config;

import com.example.qbankapi.entity.Admin;
import com.example.qbankapi.entity.Question;
import com.example.qbankapi.entity.Subject;
import com.example.qbankapi.entity.User;
import com.example.qbankapi.service.AppInitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppInitConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final AppInitService initService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Creating default users if not exists");
        List.of(
                createAdmin("admin", "admin@123"),
                createUser("sahil", "sahil@123"),
                createUser("jetha", "jetha@123"),
                createUser("daya", "daya@123"),
                createUser("champak", "champak@123"),
                createUser("tapu", "tapu@123")
        ).forEach(user -> initService.createUserIfNotExists(user));

        log.info("Creating default subjects if not exists");
        List.of(
                createSubject("Data Structures", "Organizing and storing data for efficient access and modification."),
                createSubject("Algorithms", "Step-by-step procedures for solving computational problems."),
                createSubject("Database Management Systems", "Designing and managing structured data using relational models."),
                createSubject("Operating Systems", "Managing hardware and software resources in computing environments."),
                createSubject("Computer Networks", "Transmission of data and communication between connected systems."),
                createSubject("Web Development", "Building and maintaining websites and web applications.")
        ).forEach(subject -> initService.createSubjectIfNotExists(subject));

        log.info("Creating default questions if not exists");
        List.of(
                // 20 Data Structures (subjectId = 1)
                createQuestion("<p><strong>What is the time complexity of accessing an element in an array?</strong></p>",
                        List.of("<p>O(1)</p>", "<p>O(n)</p>", "<p>O(log n)</p>", "<p>O(n log n)</p>"),
                        Question.Option.A, Question.Complexity.EASY, 1L, 1L),

                createQuestion("<p><strong>Which data structure uses FIFO principle?</strong></p>",
                        List.of("<p>Stack</p>", "<p>Queue</p>", "<p>Tree</p>", "<p>Graph</p>"),
                        Question.Option.B, Question.Complexity.EASY, 2L, 1L),

                createQuestion("<p><strong>Which data structure uses LIFO principle?</strong></p>",
                        List.of("<p>Queue</p>", "<p>Linked List</p>", "<p>Stack</p>", "<p>Array</p>"),
                        Question.Option.C, Question.Complexity.EASY, 1L, 1L),

                createQuestion("<p><strong>Which traversal algorithm uses a queue?</strong></p>",
                        List.of("<p>Inorder</p>", "<p>Postorder</p>", "<p>BFS</p>", "<p>DFS</p>"),
                        Question.Option.C, Question.Complexity.MEDIUM, 3L, 1L),

                createQuestion("<p><strong>Which data structure is best for recursive function calls?</strong></p>",
                        List.of("<p>Queue</p>", "<p>Array</p>", "<p>Stack</p>", "<p>Graph</p>"),
                        Question.Option.C, Question.Complexity.MEDIUM, 4L, 1L),

                createQuestion("<p><strong>Which structure is used for implementing LRU Cache?</strong></p>",
                        List.of("<p>HashMap + Queue</p>", "<p>HashMap + LinkedList</p>", "<p>Array</p>", "<p>Heap</p>"),
                        Question.Option.B, Question.Complexity.HARD, 5L, 1L),

                createQuestion("<p><strong>In a complete binary tree, how many children can a node have?</strong></p>",
                        List.of("<p>0</p>", "<p>1</p>", "<p>2</p>", "<p>0, 1 or 2</p>"),
                        Question.Option.D, Question.Complexity.EASY, 2L, 1L),

                createQuestion("<p><strong>What is the height of a balanced binary tree with n nodes?</strong></p>",
                        List.of("<p>O(n)</p>", "<p>O(log n)</p>", "<p>O(n log n)</p>", "<p>O(1)</p>"),
                        Question.Option.B, Question.Complexity.MEDIUM, 4L, 1L),

                createQuestion("<p><strong>Which operation is fastest in a heap?</strong></p>",
                        List.of("<p>Insert</p>", "<p>Search</p>", "<p>Delete Max</p>", "<p>Find Max</p>"),
                        Question.Option.D, Question.Complexity.MEDIUM, 3L, 1L),

                createQuestion("<p><strong>What data structure is used in a call stack?</strong></p>",
                        List.of("<p>Queue</p>", "<p>Stack</p>", "<p>Deque</p>", "<p>Set</p>"),
                        Question.Option.B, Question.Complexity.EASY, 1L, 1L),

                createQuestion("<p><strong>Which is the best data structure for implementing a priority queue?</strong></p>",
                        List.of("<p>Stack</p>", "<p>Queue</p>", "<p>Heap</p>", "<p>Tree</p>"),
                        Question.Option.C, Question.Complexity.MEDIUM, 4L, 1L),

                createQuestion("<p><strong>Which traversal visits nodes in ascending order in a BST?</strong></p>",
                        List.of("<p>Preorder</p>", "<p>Postorder</p>", "<p>Inorder</p>", "<p>BFS</p>"),
                        Question.Option.C, Question.Complexity.EASY, 2L, 1L),

                createQuestion("<p><strong>Which data structure is used to convert infix to postfix?</strong></p>",
                        List.of("<p>Queue</p>", "<p>Stack</p>", "<p>Deque</p>", "<p>Tree</p>"),
                        Question.Option.B, Question.Complexity.MEDIUM, 3L, 1L),

                createQuestion("<p><strong>Which algorithm is used to balance AVL Trees?</strong></p>",
                        List.of("<p>Merge Sort</p>", "<p>Tree Rotation</p>", "<p>Heapify</p>", "<p>Divide and Conquer</p>"),
                        Question.Option.B, Question.Complexity.HARD, 5L, 1L),

                createQuestion("<p><strong>Which structure allows O(1) access?</strong></p>",
                        List.of("<p>Linked List</p>", "<p>HashMap</p>", "<p>Tree</p>", "<p>Queue</p>"),
                        Question.Option.B, Question.Complexity.EASY, 2L, 1L),


                // 20 Algorithms (subjectId = 2)
                createQuestion("<p><strong>What is the time complexity of binary search?</strong></p>",
                        List.of("<p>O(1)</p>", "<p>O(n)</p>", "<p>O(log n)</p>", "<p>O(n log n)</p>"),
                        Question.Option.C, Question.Complexity.EASY, 1L, 2L),

                createQuestion("<p><strong>Which algorithm uses divide and conquer?</strong></p>",
                        List.of("<p>Merge Sort</p>", "<p>Bubble Sort</p>", "<p>Linear Search</p>", "<p>Insertion Sort</p>"),
                        Question.Option.A, Question.Complexity.EASY, 2L, 2L),

                createQuestion("<p><strong>What is the worst case of QuickSort?</strong></p>",
                        List.of("<p>O(n)</p>", "<p>O(n log n)</p>", "<p>O(n^2)</p>", "<p>O(log n)</p>"),
                        Question.Option.C, Question.Complexity.MEDIUM, 3L, 2L),

                createQuestion("<p><strong>Which algorithm is used for shortest path in graphs?</strong></p>",
                        List.of("<p>Dijkstra</p>", "<p>DFS</p>", "<p>BFS</p>", "<p>Prim's</p>"),
                        Question.Option.A, Question.Complexity.MEDIUM, 4L, 2L),

                createQuestion("<p><strong>Which sort is stable and non-comparison based?</strong></p>",
                        List.of("<p>Merge Sort</p>", "<p>Heap Sort</p>", "<p>Counting Sort</p>", "<p>Quick Sort</p>"),
                        Question.Option.C, Question.Complexity.HARD, 6L, 2L),

                createQuestion("<p><strong>Which algorithm finds MST?</strong></p>",
                        List.of("<p>Dijkstra</p>", "<p>Floyd-Warshall</p>", "<p>Prim's</p>", "<p>Bellman-Ford</p>"),
                        Question.Option.C, Question.Complexity.MEDIUM, 4L, 2L),

                createQuestion("<p><strong>What is Kadane’s Algorithm used for?</strong></p>",
                        List.of("<p>Longest Substring</p>", "<p>Sorting</p>", "<p>Max Subarray Sum</p>", "<p>Graph Traversal</p>"),
                        Question.Option.C, Question.Complexity.HARD, 6L, 2L),

                createQuestion("<p><strong>Which sorting algorithm is in-place?</strong></p>",
                        List.of("<p>Merge Sort</p>", "<p>Quick Sort</p>", "<p>Counting Sort</p>", "<p>Bucket Sort</p>"),
                        Question.Option.B, Question.Complexity.MEDIUM, 3L, 2L),

                createQuestion("<p><strong>Which algorithm uses greedy strategy?</strong></p>",
                        List.of("<p>Dijkstra</p>", "<p>DFS</p>", "<p>Backtracking</p>", "<p>Quick Sort</p>"),
                        Question.Option.A, Question.Complexity.MEDIUM, 3L, 2L),

                createQuestion("<p><strong>Which technique is used in DP?</strong></p>",
                        List.of("<p>Memoization</p>", "<p>Divide & Conquer</p>", "<p>Greedy</p>", "<p>All of the above</p>"),
                        Question.Option.D, Question.Complexity.MEDIUM, 4L, 2L)

        ).forEach(question -> initService.createQuestionIfNotExists(question));

    }

    private Admin createAdmin(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setModifiedAt(LocalDateTime.now());
        return admin;
    }

    private User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());
        user.setEnrolledExams(List.of());
        user.setUserExamResults(List.of());
        return user;
    }

    private Subject createSubject(String name, String description) {
        Subject subject = new Subject();
        subject.setName(name);
        subject.setDescription(description);
        subject.setQuestions(List.of());
        subject.setExams(List.of());
        return subject;
    }

    private Question createQuestion(String text, List<String> options, Question.Option correctAnswer, Question.Complexity complexity, Long marks, Long subjectId) {
        Subject subject = new Subject();
        subject.setId(subjectId);

        Question question = new Question();
        question.setText(text);
        question.setOptions(options);
        question.setCorrectAnswer(correctAnswer);
        question.setComplexity(complexity);
        question.setMarks(marks);
        question.setSubject(subject);
        return question;
    }

}
