package gui;

import dao.DAOException;
import datasource.DAOManager;
import dto.Doctor;
import dto.Incidence;
import dto.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainFrame extends javax.swing.JFrame {
    DAOManager manager = new DAOManager();
    /**
     * Creates new form gui.MainFrame
     */
    public MainFrame() throws DAOException, SQLException, NoSuchFieldException, IllegalAccessException {
        initComponents();
    }
    private Object[][] getModel (List<Incidence> incidences) throws DAOException {
        Object[][] dataForTable = new Object[incidences.size()][6];
        for (int i = 0; i < incidences.size(); i++) {
            Incidence incidence = incidences.get(i);
            dataForTable[i][0] = incidence.getId();
            dataForTable[i][1] = incidence.getDescription();
            dataForTable[i][2] = incidence.getDate();
            dataForTable[i][3] = incidence.getState();
            dataForTable[i][4] = manager.getPatientsDao().getById(incidence.getPatientId()).getName();
            dataForTable[i][5] = manager.getDoctorsDao().getById(incidence.getDoctorId()).getName();
        }
        //incidencesTable.setModel(new DefaultTableModel(dataForTable));
        return dataForTable;
    }
    private void initComponents() throws DAOException{


        JScrollPane jScrollPane1 = new JScrollPane();
        JButton exitButton = new JButton();
        JLabel patientLabel = new JLabel();
        ArrayList<String> patients = new ArrayList<>();
        List<Patient> pat = manager.getPatientsDao().getAll();
        for (Patient p : pat) {
            patients.add(p.getName());
        }
        JComboBox<String> patientCombo = new JComboBox<>(patients.toArray(new String[0]));
        JLabel doctorLabel = new JLabel();
        ArrayList<String> doctors = new ArrayList<>();
        List<Doctor> docs = manager.getDoctorsDao().getAll();
        for (Doctor doc : docs) {
            doctors.add(doc.getName());
        }
        JComboBox<String> doctorCombo = new JComboBox<>(doctors.toArray(new String[0]));
        JLabel healthInsuranceLabel = new JLabel();
        JComboBox<String> healthInsuranceCombo = new JComboBox<>();
        JLabel descriptionLabel = new JLabel();
        JTextField descriptionTextField = new JTextField();
        JLabel stateLabel = new JLabel();
        JTextField stateTextField = new JTextField();
        JLabel incidencesLabel = new JLabel();
        JLabel idIncidenceLabel = new JLabel();
        JTextField idIncidenceTextField = new JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        List<Incidence> incidences = manager.getIncidencesDao().getAll();

        DefaultTableModel model = new DefaultTableModel(getModel(incidences),new String [] {
                "id", "Descripcion", "Fecha", "Estado", "Paciente", "Doctor"
        });
        JTable incidencesTable = new JTable(model);

        ListSelectionModel selectionModel = incidencesTable.getSelectionModel();

        selectionModel.addListSelectionListener(e -> {
            final DefaultListSelectionModel target = (DefaultListSelectionModel)e.getSource();
            if(target.getAnchorSelectionIndex() == -1){
                return;
            }
            idIncidenceTextField.setText(incidencesTable.getValueAt(target.getAnchorSelectionIndex(), 0).toString());
            try {
                Incidence incidence = manager.getIncidencesDao().getById((int)incidencesTable.getValueAt(target.getAnchorSelectionIndex(), 0));
                doctorCombo.setSelectedItem(manager.getDoctorsDao().getById(incidence.getDoctorId()).getName());
                patientCombo.setSelectedItem(manager.getPatientsDao().getById(incidence.getPatientId()).getName());
                stateTextField.setText(incidence.getState());
                descriptionTextField.setText(incidence.getDescription());
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
        });
        model.addTableModelListener(e -> {
            DefaultTableModel mod;
            try {
                List<Incidence> in = manager.getIncidencesDao().getAll();
                mod = new DefaultTableModel(getModel(in), new String[]{
                        "id", "Descripcion", "Fecha", "Estado", "Paciente", "Doctor"
                });
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
            incidencesTable.setModel(mod);

        });

        JButton addButton = new JButton();
        addButton.addActionListener(e -> {
            try {
                Doctor doctor = manager.getDoctorsDao().getByName((String)doctorCombo.getSelectedItem());
                Patient patient = manager.getPatientsDao().getByName((String)patientCombo.getSelectedItem());
                Incidence incidence = new Incidence();
                incidence.setDoctorId(doctor.getId());
                incidence.setPatientId(patient.getId());
                incidence.setDate(new Date().toString());
                incidence.setState(stateTextField.getText());
                incidence.setDescription(descriptionTextField.getText());
                manager.getIncidencesDao().insert(incidence);
                Object[] data = new Object[6];
                data[0] = incidence.getId();
                data[1] = incidence.getDescription();
                data[2] = incidence.getDate();
                data[3] = incidence.getState();
                data[4] = manager.getPatientsDao().getById(incidence.getPatientId()).getName();
                data[5] = manager.getDoctorsDao().getById(incidence.getDoctorId()).getName();
                model.addRow(data);

            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
        });
        JButton deleteButton = new JButton();
        deleteButton.addActionListener(e -> {
            int row = incidencesTable.getSelectedRow();
            try {
                manager.getIncidencesDao().delete((int) incidencesTable.getValueAt(row, 0));
                model.fireTableDataChanged();
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
        });
        JButton modifyButton = new JButton();
        modifyButton.addActionListener(e -> {
            int selected = incidencesTable.getSelectedRow();
            if(selected == -1){
                return;
            }
            try {
                Doctor doctor = manager.getDoctorsDao().getByName((String)doctorCombo.getSelectedItem());
                Patient patient = manager.getPatientsDao().getByName((String)patientCombo.getSelectedItem());
                Incidence incidence = manager.getIncidencesDao().getById(Integer.valueOf(idIncidenceTextField.getText()));
                incidence.setDoctorId(doctor.getId());
                incidence.setPatientId(patient.getId());
                incidence.setState(stateTextField.getText());
                incidence.setDescription(descriptionTextField.getText());
                manager.getIncidencesDao().modify(incidence, incidence.getId());

                model.fireTableCellUpdated(selected, 0);
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }


                }
        );


        jScrollPane1.setViewportView(incidencesTable);

        exitButton.setText("Salir");
        exitButton.addActionListener(e -> System.exit(0));

        patientLabel.setText("Paciente");

        //patientCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        doctorLabel.setText("Doctor");

        //doctorCombo.setModel(new DefaultComboBoxModel<String>(doctors.toArray(new String[doctors.size()])));

        healthInsuranceLabel.setText("Obra Social");
        healthInsuranceLabel.setVisible(false);
        healthInsuranceCombo.setVisible(false);
        descriptionLabel.setText("Descripcion");

        //healthInsuranceCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        stateLabel.setText("Estado");

        incidencesLabel.setText("Incidencias Cargadas");

        addButton.setText("Agregar");

        deleteButton.setText("Eliminar");

        modifyButton.setText("Modificar");

        idIncidenceLabel.setText("ID");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                                .addComponent(descriptionLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                                                                                .addComponent(patientLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                        .addComponent(idIncidenceLabel))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(patientCombo, 0, 99, Short.MAX_VALUE)
                                                                                        .addComponent(idIncidenceTextField))
                                                                                .addGap(47, 47, 47)
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(stateLabel)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                .addComponent(stateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(doctorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(doctorCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                .addGap(74, 74, 74)
                                                                                .addComponent(healthInsuranceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(healthInsuranceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(descriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(addButton)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(modifyButton)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(deleteButton)
                                                                                .addGap(2, 2, 2))))
                                                        .addComponent(incidencesLabel)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 841, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(idIncidenceLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(idIncidenceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(doctorLabel)
                                                .addComponent(doctorCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(healthInsuranceLabel)
                                                .addComponent(healthInsuranceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(22, 83, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(descriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(addButton)
                                                                                .addComponent(deleteButton)
                                                                                .addComponent(modifyButton)))
                                                                .addGap(32, 32, 32)
                                                                .addComponent(incidencesLabel)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(30, 30, 30)
                                                                .addComponent(exitButton))
                                                        .addComponent(descriptionLabel))
                                                .addGap(20, 20, 20))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(patientLabel)
                                                        .addComponent(patientCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(stateLabel)
                                                        .addComponent(stateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new MainFrame().setVisible(true);
            } catch (DAOException | SQLException | NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // End of variables declaration
}
