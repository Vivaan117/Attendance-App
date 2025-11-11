import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import javax.swing.text.JTextComponent; // ADDED: Fixes 'cannot find symbol JTextComponent'

public class AttendanceManagerSwing extends JFrame {

    private CardLayout rootCardLayout = new CardLayout();
    private JPanel rootPanel = new JPanel(rootCardLayout);

    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton loginBtn = new JButton("Login");

    private JLabel totalClassesLabel = new JLabel("0", SwingConstants.CENTER);
    private JLabel attendedClassesLabel = new JLabel("0", SwingConstants.CENTER);
    private JLabel attendanceRateLabel = new JLabel("0%", SwingConstants.CENTER);
    private JComboBox<String> subjectCombo;
    private JComboBox<String> statusCombo;
    private JButton markBtn = new JButton("Mark Attendance");
    private JTable attendanceTable;
    private DefaultTableModel attendanceTableModel;
    private JButton deleteBtn = new JButton("Delete Selected");
    private JButton logoutBtn = new JButton("Logout");

    private JSpinner targetSpinner;
    private JSpinner upcomingSpinner;
    private JButton predictBtn = new JButton("Calculate");
    private JTextArea predictorResultArea = new JTextArea();

    private final List<AttendanceRecord> attendanceRecords = new ArrayList<>();
    private static final int MAX_RECORDS = 999;

    private String[] subjects = {"Advance Object Oriented Programming", "Data Structure and Algorithm", "Transforms and boundary value problems", "Operating Systems"};
    private final DateTimeFormatter displayDateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    public AttendanceManagerSwing() {
        super("Attendance Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 640);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(880, 620));

        initUI();
    }

    private void initUI() {
        rootPanel.add(buildLoginPanel(), "login");
        rootPanel.add(buildMainPanel(), "main");

        add(rootPanel);
        rootCardLayout.show(rootPanel, "login");
    }

    private JPanel buildLoginPanel() {
        JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(new Color(245, 247, 249)); 

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        // FIX: Replaced CompoundBorder with just EmptyBorder
        box.setBorder(new EmptyBorder(35, 35, 35, 35)); 
        box.setBackground(Color.WHITE);
        box.setPreferredSize(new Dimension(420, 350)); 
        box.setMaximumSize(new Dimension(420, 350)); // Set max size too

        JLabel title = new JLabel("Attendance Manager");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f)); 
        title.setForeground(new Color(33, 150, 243)); 
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Track and manage attendance");
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 14f));
        subtitle.setForeground(new Color(100, 100, 100)); 
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        styleTextField(usernameField);
        styleTextField(passwordField);

        box.add(Box.createVerticalGlue()); 
        box.add(title);
        box.add(Box.createRigidArea(new Dimension(0, 8)));
        box.add(subtitle);
        box.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel userLabel = new JLabel("USERNAME");
        userLabel.setFont(userLabel.getFont().deriveFont(Font.BOLD, 10f));
        userLabel.setForeground(new Color(150, 150, 150));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // FIX: Align label left
        box.add(userLabel);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT); // FIX: Ensure field alignment
        box.add(usernameField);
        box.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel pwLabel = new JLabel("PASSWORD");
        pwLabel.setFont(pwLabel.getFont().deriveFont(Font.BOLD, 10f));
        pwLabel.setForeground(new Color(150, 150, 150));
        pwLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // FIX: Align label left
        box.add(pwLabel);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT); // FIX: Ensure field alignment
        box.add(passwordField);
        box.add(Box.createRigidArea(new Dimension(0, 25)));

        styleButton(loginBtn, new Color(33, 150, 243));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48)); 
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // FIX: Ensure button alignment
        
        // REMOVED: Custom border that might have caused text issues
        // loginBtn.setBorder(new CompoundBorder(
        //     new LineBorder(new Color(33, 150, 243), 1, true),
        //     new EmptyBorder(12, 12, 12, 12)
        // ));
        
        box.add(loginBtn);
        box.add(Box.createVerticalGlue()); 

        loginBtn.addActionListener(e -> {
            String u = usernameField.getText().trim();
            String pw = new String(passwordField.getPassword()).trim();
            if (u.isEmpty() || pw.isEmpty()) {
                showToast("Enter username and password", Color.RED);
                return;
            }
            rootCardLayout.show(rootPanel, "main");
            updateDashboard();
            showToast("Welcome, " + u + "!", new Color(33, 150, 243));
        });

        passwordField.addActionListener(e -> loginBtn.doClick());

        bg.add(box);
        return bg;
    }
    
    // Helper method for text field styling
    private void styleTextField(JTextComponent comp) {
        comp.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220)),
            new EmptyBorder(10, 12, 10, 12) 
        ));
        comp.setFont(comp.getFont().deriveFont(Font.PLAIN, 15f));
        comp.setBackground(new Color(250, 250, 250)); 
    }
    
    private JPanel buildMainPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(245, 247, 249));

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(12, 18, 12, 18));
        header.setBackground(new Color(33, 150, 243));
        JLabel appTitle = new JLabel("Attendance Manager");
        appTitle.setForeground(Color.WHITE);
        appTitle.setFont(appTitle.getFont().deriveFont(Font.BOLD, 18f));
        header.add(appTitle, BorderLayout.WEST);

        styleButton(logoutBtn, new Color(244, 67, 54)); // FIX: Ensure button is styled
        header.add(logoutBtn, BorderLayout.EAST);

        logoutBtn.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            rootCardLayout.show(rootPanel, "login");
            showToast("Logged out successfully", new Color(33, 150, 243));
        });

        main.add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", buildDashboardPanel());
        tabs.addTab("Predictor", buildPredictorPanel());

        main.add(tabs, BorderLayout.CENTER);
        return main;
    }

    private JPanel buildDashboardPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(16, 20, 16, 20));
        p.setBackground(new Color(245, 247, 249));

        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 14, 0));
        statsGrid.setOpaque(false);
        statsGrid.add(makeStatCard("Total Classes", totalClassesLabel, new Color(66, 133, 244)));
        statsGrid.add(makeStatCard("Classes Attended", attendedClassesLabel, new Color(76, 175, 80)));
        statsGrid.add(makeStatCard("Attendance Rate", attendanceRateLabel, new Color(255, 193, 7)));
        p.add(statsGrid, BorderLayout.NORTH);

        JPanel markPanel = new JPanel();
        markPanel.setLayout(new BoxLayout(markPanel, BoxLayout.Y_AXIS));
        markPanel.setBackground(Color.WHITE);
        markPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 225, 230)),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel markTitle = new JLabel("Mark Attendance");
        markTitle.setFont(markTitle.getFont().deriveFont(Font.BOLD, 16f));
        markPanel.add(markTitle);
        markPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        subjectCombo = new JComboBox<>(subjects);
        statusCombo = new JComboBox<>(new String[]{"Present", "Absent"});
        styleButton(markBtn, new Color(76, 175, 80)); // FIX: Ensure button is styled
        row.add(new JLabel("Subject:"));
        row.add(subjectCombo);
        row.add(new JLabel("Status:"));
        row.add(statusCombo);
        row.add(markBtn);
        markPanel.add(row);
        markBtn.addActionListener(e -> markAttendanceAction());

        attendanceTableModel = new DefaultTableModel(new Object[]{"ID", "Date", "Subject", "Status"}, 0);
        attendanceTable = new JTable(attendanceTableModel);
        attendanceTable.setFillsViewportHeight(true);
        attendanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        attendanceTable.getColumnModel().getColumn(0).setMinWidth(0);
        attendanceTable.getColumnModel().getColumn(0).setMaxWidth(0);
        JScrollPane scroll = new JScrollPane(attendanceTable);

        styleButton(deleteBtn, new Color(244, 67, 54));
        deleteBtn.setEnabled(false);
        attendanceTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) ->
                deleteBtn.setEnabled(attendanceTable.getSelectedRow() >= 0));
        deleteBtn.addActionListener(e -> deleteSelectedRecord());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(deleteBtn);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 225, 230)),
                new EmptyBorder(8, 8, 8, 8)
        ));
        listPanel.setBackground(Color.WHITE);
        listPanel.add(new JLabel("Recent Attendance", SwingConstants.LEFT), BorderLayout.NORTH);
        listPanel.add(scroll, BorderLayout.CENTER);
        listPanel.add(bottom, BorderLayout.SOUTH);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        center.add(markPanel, BorderLayout.NORTH);
        center.add(listPanel, BorderLayout.CENTER);
        p.add(center, BorderLayout.CENTER);

        return p;
    }

    private JPanel buildPredictorPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.setBackground(new Color(245, 247, 249));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 225, 230)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel title = new JLabel("Attendance Predictor");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        targetSpinner = new JSpinner(new SpinnerNumberModel(75, 0, 100, 1));
        upcomingSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 1000, 1));
        styleButton(predictBtn, new Color(33, 150, 243)); // FIX: Ensure button is styled
        row.add(new JLabel("Target (%)"));
        row.add(targetSpinner);
        row.add(new JLabel("Upcoming Classes"));
        row.add(upcomingSpinner);
        row.add(predictBtn);
        card.add(row);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        predictorResultArea.setEditable(false);
        predictorResultArea.setLineWrap(true);
        predictorResultArea.setWrapStyleWord(true);
        predictorResultArea.setBackground(new Color(248, 249, 250));
        predictorResultArea.setFont(predictorResultArea.getFont().deriveFont(14f));
        predictorResultArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.add(predictorResultArea);

        predictBtn.addActionListener(e -> performPrediction());
        p.add(card, BorderLayout.NORTH);
        return p;
    }

    private void markAttendanceAction() {
        if (attendanceRecords.size() >= MAX_RECORDS) {
            showToast("Max 999 records reached.", Color.RED);
            return;
        }
        String subject = (String) subjectCombo.getSelectedItem();
        String status = ((String) statusCombo.getSelectedItem()).toLowerCase();
        if (subject == null || subject.isEmpty()) return;

        attendanceRecords.add(new AttendanceRecord(UUID.randomUUID().toString(),
                LocalDateTime.now(), subject, status, System.currentTimeMillis()));
        refreshAttendanceTable();
        updateDashboard();
        showToast("Attendance marked!", new Color(76, 175, 80));
    }

    private void refreshAttendanceTable() {
        attendanceTableModel.setRowCount(0);
        attendanceRecords.sort(Comparator.comparingLong(AttendanceRecord::getTimestamp).reversed());
        for (AttendanceRecord r : attendanceRecords) {
            attendanceTableModel.addRow(new Object[]{r.getId(), r.getDate().format(displayDateFormat), r.getSubject(),
                    r.getStatus().equals("present") ? "✅ Present" : "❌ Absent"});
        }
    }

    private void updateDashboard() {
        int total = attendanceRecords.size();
        long attended = attendanceRecords.stream().filter(r -> r.getStatus().equals("present")).count();
        double rate = total > 0 ? ((double) attended / total) * 100.0 : 0.0;
        totalClassesLabel.setText(String.valueOf(total));
        attendedClassesLabel.setText(String.valueOf(attended));
        attendanceRateLabel.setText(String.format("%.1f%%", rate));
    }

    private void deleteSelectedRecord() {
        int row = attendanceTable.getSelectedRow();
        if (row < 0) return;
        String id = (String) attendanceTableModel.getValueAt(row, 0);
        attendanceRecords.removeIf(r -> r.getId().equals(id));
        refreshAttendanceTable();
        updateDashboard();
        showToast("Record deleted", new Color(244, 67, 54));
    }

    private void performPrediction() {
        int target = (Integer) targetSpinner.getValue();
        int upcoming = (Integer) upcomingSpinner.getValue();
        int total = attendanceRecords.size();
        long attended = attendanceRecords.stream().filter(r -> r.getStatus().equals("present")).count();
        if (total == 0) {
            showToast("No attendance data found.", Color.RED);
            return;
        }
        double currentPct = ((double) attended / total) * 100;
        int requiredAttended = (int) Math.ceil((target / 100.0) * (total + upcoming));
        int need = Math.max(0, requiredAttended - (int) attended);
        predictorResultArea.setText(String.format("Current: %.1f%% (%d/%d)\nTarget: %d%%\nUpcoming: %d\n\nNeed to attend %d classes.",
                currentPct, attended, total, target, upcoming, need));
    }

    private JPanel makeStatCard(String title, JLabel value, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new MatteBorder(4, 0, 0, 0, accent),
                new EmptyBorder(16, 16, 16, 16)
        ));
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setForeground(Color.DARK_GRAY);
        lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN, 13f));
        value.setFont(value.getFont().deriveFont(Font.BOLD, 26f));
        card.add(lbl, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        return card;
    }

   private void styleButton(JButton b, Color c) {
    // 1. Set background color (Blue, Green, or Red)
    b.setBackground(c);
    
    // 2. Remove default focus painting
    b.setFocusPainted(false);
    
    // 3. Set a standard border with padding
    b.setBorder(new EmptyBorder(10, 16, 10, 16)); 
    
    // 4. Set cursor
    b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    // FIX: Change foreground color (text color) to BLACK for maximum contrast
    // This addresses the issue where the L&F was overriding the white color.
    b.setForeground(Color.BLACK); 
}

    private void showToast(String message, Color bg) {
        JWindow toast = new JWindow();
        JLabel label = new JLabel(message);
        label.setOpaque(true);
        label.setBackground(bg);
        label.setForeground(Color.WHITE);
        label.setBorder(new EmptyBorder(10, 16, 10, 16));
        toast.add(label);
        toast.pack();

        Point loc = getLocationOnScreen();
        int x = loc.x + getWidth() - toast.getWidth() - 30;
        int y = loc.y + getHeight() - toast.getHeight() - 60;
        toast.setLocation(x, y);
        toast.setVisible(true);
        new javax.swing.Timer(2200, e -> toast.dispose()).start();
    }

    private static class AttendanceRecord {
        private final String id;
        private final LocalDateTime date;
        private final String subject;
        private final String status;
        private final long timestamp;

        public AttendanceRecord(String id, LocalDateTime date, String subject, String status, long timestamp) {
            this.id = id;
            this.date = date;
            this.subject = subject;
            this.status = status;
            this.timestamp = timestamp;
        }
        public String getId() { return id; }
        public LocalDateTime getDate() { return date; }
        public String getSubject() { return subject; }
        public String getStatus() { return status; }
        public long getTimestamp() { return timestamp; }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new AttendanceManagerSwing().setVisible(true));
    }
}