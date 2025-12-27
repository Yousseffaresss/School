class Teacher {
    int id;
    String name;
    String subjectExpertise; // e.g., Math

    public Teacher(int id, String name, String subjectExpertise) {
        this.id = id;
        this.name = name;
        this.subjectExpertise = subjectExpertise;
    }

    public String toFileString() {
        return id + "|" + escape(name) + "|" + escape(subjectExpertise);
    }

    public static Teacher fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        return new Teacher(Integer.parseInt(p[0]), unescape(p[1]), unescape(p[2]));
    }

    private static String escape(String s) { return s.replace("|", "/|"); }
    private static String unescape(String s) { return s.replace("/|", "|"); }

    @Override
    public String toString() {
        return String.format("ID:%d | %s | Expertise:%s", id, name, subjectExpertise);
    }static void teachersMenu() {
        while (true) {
            System.out.println("\n--- Teachers Menu ---");
            System.out.println("1. Add Teacher");
            System.out.println("2. List Teachers");
            System.out.println("3. Search Teacher by ID");
            System.out.println("4. Update Teacher");
            System.out.println("5. Delete Teacher");
            System.out.println("6. Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1" -> addTeacher();
                case "2" -> listTeachers();
                case "3" -> searchTeacher();
                case "4" -> updateTeacher();
                case "5" -> deleteTeacher();
                case "6" -> { return; }
                default -> System.out.println("Invalid.");
            }
        }
    }

    static void addTeacher() {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Subject Expertise: ");
        String subj = sc.nextLine();
        Teacher t = new Teacher(nextTeacherId++, name, subj);
        teachers.add(t);
        System.out.println("Teacher added: " + t);
    }

    static void listTeachers() {
        if (teachers.isEmpty()) { System.out.println("No teachers."); return; }
        for (Teacher t : teachers) System.out.println(t);
    }

    static void searchTeacher() {
        System.out.print("Enter teacher ID: ");
        int id = readIntSafe();
        Teacher t = findTeacherById(id);
        if (t == null) System.out.println("Not found.");
        else System.out.println(t);
    }

    static void updateTeacher() {
        System.out.print("Enter teacher ID to update: ");
        int id = readIntSafe();
        Teacher t = findTeacherById(id);
        if (t == null) { System.out.println("Not found."); return; }
        System.out.println("Current: " + t);
        System.out.print("New Name (leave empty keep): ");
        String name = sc.nextLine();
        if (!name.isEmpty()) t.name = name;
        System.out.print("New Expertise (leave empty keep): ");
        String ex = sc.nextLine();
        if (!ex.isEmpty()) t.subjectExpertise = ex;
        System.out.println("Updated: " + t);
    }

    static void deleteTeacher() {
        System.out.print("Enter teacher ID to delete: ");
        int id = readIntSafe();
        boolean removed = teachers.removeIf(t -> t.id == id);
        // set teacherId in courses to 0 (unassigned)
        for (Course c : courses) if (c.teacherId == id) c.teacherId = 0;
        if (removed) System.out.println("Deleted teacher. Courses unassigned.");
        else System.out.println("No such teacher.");
    }

    static Teacher findTeacherById(int id) {
        return teachers.stream().filter(t -> t.id == id).findFirst().orElse(null);
    }