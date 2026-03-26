package miappui.frontend.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import mdlaf.MaterialLookAndFeel;

public class dashboard extends JFrame {

    private JTextField textField;
    private JTextArea textArea;
    private JTable table;
    private JComboBox<String> comboBox;
    private JCheckBox chkPendientes;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private DefaultTableModel modelo;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new MaterialLookAndFeel());
                new dashboard().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public dashboard() {
        setTitle("Gestión de Tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 450);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout(10,10));
        contentPane.setBorder(new EmptyBorder(10,10,10,10));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);

        // ========================
        // PANEL IZQUIERDO (FORM)
        // ========================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        textField = new JTextField();
        textField.setAlignmentY(Component.TOP_ALIGNMENT);
        textArea = new JTextArea(4, 20);

        String[] opciones = {"Seleccionar","Pendiente", "En Progreso", "Completada"};
        comboBox = new JComboBox<>(opciones);

        btnSave = new JButton("Guardar");

        leftPanel.add(new JLabel("Título"));
        leftPanel.add(textField);
        textField.setMaximumSize(new Dimension(300, 50)); // ancho, alto
        leftPanel.add(textField);
        leftPanel.add(Box.createVerticalStrut(10));

        leftPanel.add(new JLabel("Estado"));
        leftPanel.add(comboBox);
        comboBox.setMaximumSize(new Dimension(300, 30));
        leftPanel.add(Box.createVerticalStrut(10));

        leftPanel.add(new JLabel("Descripción"));
        leftPanel.add(new JScrollPane(textArea));
        leftPanel.add(Box.createVerticalStrut(15));

        leftPanel.add(btnSave);

        contentPane.add(leftPanel, BorderLayout.WEST);

        // ========================
        // PANEL DERECHO
        // ========================
        JPanel rightPanel = new JPanel(new BorderLayout(10,10));
        rightPanel.setBackground(Color.LIGHT_GRAY);

        btnUpdate = new JButton("Actualizar");
        btnDelete = new JButton("Eliminar");

        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        chkPendientes = new JCheckBox("Solo pendientes");
        topButtons.setBackground(Color.WHITE);
        topButtons.add(chkPendientes);
        topButtons.add(btnDelete);
        topButtons.add(btnUpdate);

        // Tabla
        modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Título", "Descripción", "Estado"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(modelo);
        table.setRowHeight(25);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);

        rightPanel.add(topButtons, BorderLayout.NORTH);
        rightPanel.add(scroll, BorderLayout.CENTER);

        contentPane.add(rightPanel, BorderLayout.CENTER);

        // ========================
        // EVENTOS
        // ========================

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int fila = table.getSelectedRow();

                textField.setText(modelo.getValueAt(fila, 0).toString());
                textArea.setText(modelo.getValueAt(fila, 1).toString());
                comboBox.setSelectedItem(modelo.getValueAt(fila, 2).toString());

                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                btnSave.setEnabled(false);
            }
        });

        btnSave.addActionListener(e -> {
            String titulo = textField.getText();
            String descripcion = textArea.getText();
            String estado = (String) comboBox.getSelectedItem();

            if (!titulo.isEmpty() && !descripcion.isEmpty() && !estado.equals("Seleccionar")) {
                modelo.addRow(new Object[]{titulo, descripcion, estado});
                limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "Completar todos los campos");
            }
        });

        btnUpdate.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila != -1) {
                modelo.setValueAt(comboBox.getSelectedItem(), fila, 2);
                limpiar();
            }
        });

        btnDelete.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila != -1) {
                int opcion = JOptionPane.showConfirmDialog(
                        null,
                        "¿Seguro que deseas eliminar?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    modelo.removeRow(fila);
                    limpiar();
                }
            }
        });
        
        TableRowSorter sorter = new TableRowSorter<>(modelo);
        table.setRowSorter(sorter);
        
        chkPendientes.addActionListener(e -> {
            if (chkPendientes.isSelected()) {
                sorter.setRowFilter(RowFilter.regexFilter("^Pendiente$", 2));
            } else {
                sorter.setRowFilter(null);
            }
        });
    }
    

    private void limpiar() {
        textField.setText("");
        textArea.setText("");
        comboBox.setSelectedIndex(0);
        table.clearSelection();

        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
    }
    
    public void styleTable(JTable table) {

        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(33,150,243));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(33,150,243));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }
    
}