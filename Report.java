static void reportsMenu() {
        while (true) {
            System.out.println("\n--- Reports ---");
            System.out.println("1. Student Transcript (grades)");
            System.out.println("2. Students in a Course");
            System.out.println("3. Courses taught by Teacher");
            System.out.println("4. Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1" -> studentTranscript();
                case "2" -> studentsInCourse();
                case "3" -> coursesByTeacher();
                case "4" -> { return; }
                default -> System.out.println("Invalid.");
            }
        }
    }

    static void studentTranscript() {
        System.out.print("Enter student ID: ");
        int sid = readIntSafe();
        Student s = findStudentById(sid);
        if (s == null) { System.out.println("No such student."); return; }
        System.out.println("Transcript for " + s.name);
        boolean any = false;
        for (Enrollment e : enrollments) {
            if (e.studentId == sid) {
                Course c = findCourseById(e.courseId);
                String cname = (c == null) ? "UnknownCourse" : c.title;
                String g = (e.grade < 0) ? "N/A" : String.format("%.2f", e.grade);
                System.out.println("Course: " + cname + " | Grade: " + g);
                any = true;
            }
        }
        if (!any) System.out.println("No enrollments for this student.");
    }

    static void studentsInCourse() {
        System.out.print("Enter course ID: ");
        int cid = readIntSafe();
        Course c = findCourseById(cid);
        if (c == null) { System.out.println("No such course."); return; }
        System.out.println("Students in " + c.title);
        boolean any = false;
        for (Enrollment e : enrollments) {
            if (e.courseId == cid) {
                Student s = findStudentById(e.studentId);
                String sname = (s == null) ? "UnknownStudent" : s.name;
                String g = (e.grade < 0) ? "N/A" : String.format("%.2f", e.grade);
                System.out.println("StudentID:" + e.studentId + " | " + sname + " | Grade:" + g);
                any = true;
            }
        }
        if (!any) System.out.println("No students enrolled in this course.");
    }

    static void coursesByTeacher() {
        System.out.print("Enter teacher ID: ");
        int tid = readIntSafe();
        Teacher t = findTeacherById(tid);
        if (t == null) { System.out.println("No such teacher."); return; }
        System.out.println("Courses taught by " + t.name);
        boolean any = false;
        for (Course c : courses) {
            if (c.teacherId == tid) {
                System.out.println(c);
                any = true;
            }
        }
        if (!any) System.out.println("No courses assigned to this teacher.");
    }

