package com.general.mbts4ma.view.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.general.mbts4ma.Constraint;
import com.general.mbts4ma.EventInstance;
import com.general.mbts4ma.view.MainView;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;
import com.github.eta.esg.Vertex;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class ConstraintsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tblConstraints;
	private JComboBox<String> jcomboboxLeft;
	private JComboBox<String> jcomboboxRight;
	private JComboBox<String> jcomboboxLogicalConnector;

	private GraphProjectVO graphProject = null;
	private mxGraph graph = null;
	private ArrayList<Constraint> values = null;
	private ArrayList<Constraint> temporaryConstraints = null;
	private List<String> verticesIdLeftEvent = null;

	private void loadTableConstraints() {
		String col[] = { "id", "Left Event", "Logical Connector", "Right Event" };
		this.tblConstraints.setModel(new DefaultTableModel(col, 0));
		for (int i = 0; i < this.tblConstraints.getColumnCount(); i++) {
			this.tblConstraints.getColumnModel().getColumn(i).setPreferredWidth(175);
		}
		if (this.values != null && !this.values.isEmpty()) {
			for (Constraint c : this.values) {
				if (c.getLeft() == null)
					System.out.println("c.getLeft() == NULL, ID = " + c.getId());
				if (c.getRight() == null)
					System.out.println("c.getRight() == NULL, ID = " + c.getId());
				((DefaultTableModel) this.tblConstraints.getModel()).addRow(new Object[] { c.getId(),
						c.getLeft().toString(), c.getLogicalConnector(), c.getRight().toString() });
			}
		}
		// SCROLL FOR TABLE
		Dimension preferredTableSize = this.tblConstraints.getPreferredSize();
		preferredTableSize.height = 300;
		preferredTableSize.width = 450;
		this.tblConstraints.setPreferredScrollableViewportSize(preferredTableSize);
	}

	private EventInstance getEventInstanceById(String id) {
		System.out.println("getEventInstanceById para id = " + id);
		for (Constraint c : this.temporaryConstraints) {
			if (c.getLeft() != null && c.getLeft().getId().equalsIgnoreCase(id))
				return c.getLeft();
			else if (c.getRight() != null && c.getRight().getId().equalsIgnoreCase(id))
				return c.getRight();
		}
		System.out.println("getEventInstanceById para id = " + id + " retornando NULL");
		return null;
	}

	private void addItemListenerOnCombobox() {
		this.jcomboboxLeft.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != 1)
					return;
				String verticeId = "";
				String selectedEvent = (String) e.getItem();
				verticeId = graphProject.getVerticeIdByEventInstace(selectedEvent);
				if (verticeId.equals(""))
					if (jcomboboxLeft.getSelectedIndex() != 0)
						verticeId = getVerticeIdByEventName((String) e.getItem());
				loadRightCombobox(verticeId);
			}
		});
	}

	private String eventNameGenerator(mxCell cell) {
		StringBuilder sb = new StringBuilder();
		for (String s : ((String) cell.getValue()).toLowerCase().replaceAll("[^a-zA-Z ]+", "").split(" ")) {
			sb.append(s.charAt(0));
		}
		return sb.toString();
	}

	private List<mxCell> getCellsOnGraph() {
		Object[] allNodes = this.graph.getChildVertices(graph.getDefaultParent());
		ArrayList<mxCell> cells = new ArrayList<mxCell>();
		for (int i = 0; i < allNodes.length; i++) {
			if (!((mxCell) allNodes[i]).getId().equalsIgnoreCase(MainView.ID_START_VERTEX)
					&& !((mxCell) allNodes[i]).getId().equalsIgnoreCase(MainView.ID_END_VERTEX))
				cells.add((mxCell) allNodes[i]);
		}
		return cells;
	}

	private String getVerticeIdByEventName(String eventName) {
		List<mxCell> cells = this.getCellsOnGraph();
		for (mxCell c : cells) {
			if (eventNameGenerator(c).equalsIgnoreCase(eventName))
				return c.getId();
		}
		return "";
	}

	private void loadLeftCombobox() {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		model.addElement("Left Event");

		List<String> elements = new ArrayList<String>();

		List<mxCell> vertices = getCellsOnGraph();
		for (mxCell c : vertices) {
			this.verticesIdLeftEvent.add(c.getId());
			elements.add(eventNameGenerator(c));
			// model.addElement(eventNameGenerator(c));
		}

		Map<String, ArrayList<EventInstance>> eventMap = this.graphProject.getEventInstanceByVertices();
		for (String key : eventMap.keySet()) {
			for (EventInstance ei : eventMap.get(key))
				elements.add(ei.toString());
			// model.addElement(ei.toString());
		}

		Collections.sort(elements);
		for (String s : elements)
			model.addElement(s);

		this.jcomboboxLeft.setModel(model);
	}

	private void loadRightCombobox(String verticeId) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		model.addElement("Right Event");
		Map<String, ArrayList<EventInstance>> eventMap = this.graphProject.getEventInstanceByVertices();

		List<String> elements = new ArrayList<String>();

		List<mxCell> vertices = getCellsOnGraph();
		for (mxCell c : vertices) {
			if (!c.getId().equals(verticeId))
				elements.add(eventNameGenerator(c));
			// model.addElement(eventNameGenerator(c));
		}

		for (String key : eventMap.keySet()) {
			if (!key.equals(verticeId)) {
				for (EventInstance ei : eventMap.get(key))
					elements.add(ei.toString());
				// model.addElement(ei.toString());
			}
		}

		Collections.sort(elements);
		for (String s : elements)
			model.addElement(s);

		this.jcomboboxRight.setModel(model);

	}

	public ConstraintsDialog(GraphProjectVO graphProject, mxGraph graph) {
		this.graphProject = graphProject;
		this.graph = graph;

		this.values = graphProject.getConstraints();
		this.temporaryConstraints = new ArrayList<Constraint>(this.values);
		this.verticesIdLeftEvent = new ArrayList<String>();

		this.setTitle("Constraints");

		this.setBounds(100, 100, 360 + 150, 250 + 250);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setModal(true);
		this.setLocationRelativeTo(null);

		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);

		this.tblConstraints = new JTable(0, 0);

		this.tblConstraints.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.tblConstraints.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.tblConstraints.getTableHeader().setReorderingAllowed(false);

		this.contentPanel.add(this.tblConstraints);

		{ // BEGIN: CONSTRAINT FORM
			this.jcomboboxLeft = new JComboBox<String>();
			this.jcomboboxRight = new JComboBox<String>();
			this.jcomboboxLogicalConnector = new JComboBox<String>();
			jcomboboxLogicalConnector.addItem("\u2192");
			jcomboboxLogicalConnector.addItem("\u2194");
			JButton btnConstraintConfirm = new JButton("");
			btnConstraintConfirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ConstraintsDialog.this.addConstraint();
				}
			});
			btnConstraintConfirm.setIcon(new ImageIcon(ConstraintsDialog.class
					.getResource("/com/general/mbts4ma/view/framework/images/addconstraint.png")));
			btnConstraintConfirm.setToolTipText("Add");
			JButton btnConstraintClean = new JButton("");
			btnConstraintClean.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ConstraintsDialog.this.clean();
				}
			});
			btnConstraintClean.setIcon(new ImageIcon(
					ConstraintsDialog.class.getResource("/com/general/mbts4ma/view/framework/images/clean.png")));
			btnConstraintClean.setToolTipText("Delete");
			((JLabel) jcomboboxLogicalConnector.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			JPanel buttonPaneConstraint = new JPanel();
			this.getContentPane().add(buttonPaneConstraint, BorderLayout.NORTH);
			GroupLayout gl_buttonPane = new GroupLayout(buttonPaneConstraint);
			gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.CENTER).addGroup(gl_buttonPane
					.createSequentialGroup().addContainerGap()
					.addComponent(this.jcomboboxLeft, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
					.addComponent(jcomboboxLogicalConnector, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
					.addComponent(this.jcomboboxRight, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
					.addComponent(btnConstraintClean, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
					.addComponent(btnConstraintConfirm, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addContainerGap()));
			gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup().addContainerGap()
							.addGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING)
									.addComponent(this.jcomboboxLeft, GroupLayout.PREFERRED_SIZE, 45,
											GroupLayout.PREFERRED_SIZE)
									.addComponent(jcomboboxLogicalConnector, GroupLayout.PREFERRED_SIZE, 45,
											GroupLayout.PREFERRED_SIZE)
									.addComponent(this.jcomboboxRight, GroupLayout.PREFERRED_SIZE, 45,
											GroupLayout.PREFERRED_SIZE)
									.addComponent(btnConstraintClean, GroupLayout.PREFERRED_SIZE, 45,
											GroupLayout.PREFERRED_SIZE)
									.addComponent(btnConstraintConfirm, GroupLayout.PREFERRED_SIZE, 45,
											GroupLayout.PREFERRED_SIZE))
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
			buttonPaneConstraint.setLayout(gl_buttonPane);
		} // END: CONSTRAINT FORM

		{
			JPanel buttonPane = new JPanel();
			this.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			JButton btnConfirm = new JButton("");
			btnConfirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ConstraintsDialog.this.confirm();
				}
			});
			btnConfirm.setIcon(new ImageIcon(
					ConstraintsDialog.class.getResource("/com/general/mbts4ma/view/framework/images/confirm.png")));
			btnConfirm.setToolTipText("Confirm");
			JButton btnCancel = new JButton("");
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog(null, "Remove All Constraints?") == JOptionPane.YES_OPTION) {
						ConstraintsDialog.this.cancel();
					}
				}
			});
			btnCancel.setIcon(new ImageIcon(
					ConstraintsDialog.class.getResource("/com/general/mbts4ma/view/framework/images/cancel.png")));
			btnCancel.setToolTipText("Delete");

			JButton btnDeleteRow = new JButton("");
			btnDeleteRow.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ConstraintsDialog.this.deleteRowTable();
				}
			});
			btnDeleteRow.setIcon(new ImageIcon(
					ConstraintsDialog.class.getResource("/com/general/mbts4ma/view/framework/images/deleterow.png")));
			btnDeleteRow.setToolTipText("Delete selected row");

			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup().addContainerGap()
							.addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
							.addComponent(btnDeleteRow, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addContainerGap()));
			gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane
					.createSequentialGroup().addContainerGap()
					.addGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING)
							.addComponent(btnDeleteRow, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
			buttonPane.setLayout(gl_buttonPane);
		}

		this.contentPanel.add(new JScrollPane(this.tblConstraints));

		this.loadLeftCombobox();
		this.loadRightCombobox("");
		this.addItemListenerOnCombobox();
		this.loadTableConstraints();
	}

	private int validateConstraint() {
		if (this.jcomboboxLeft.getSelectedIndex() == 0 || this.jcomboboxRight.getSelectedIndex() == 0)
			return -1;
		String id = this.jcomboboxLeft.getSelectedItem() + getSymbolLogicalConnector(getLogicalConnector())
				+ this.jcomboboxRight.getSelectedItem();
		for (Constraint c : this.temporaryConstraints) {
			if (c.getId().equals(id))
				return 1;
		}
		return 0;
	}

	private String getLogicalConnector() {
		String retorno = "conditional";
		if (this.jcomboboxLogicalConnector.getSelectedIndex() == 1)
			retorno = "biconditional";
		return retorno;
	}

	private String getSymbolLogicalConnector(String s) {
		if (s.equals("conditional"))
			return "-->";
		return "<-->";
	}

	private void addConstraint() {
		int validate = validateConstraint();
		if (validate == 1) {
			JOptionPane.showMessageDialog(null, "Constraint already exists!");
			return;
		} else if (validate == -1) {
			JOptionPane.showMessageDialog(null, "Choose a valid Event!");
			return;
		}

		String leftJcomboboxText = (String) this.jcomboboxLeft.getSelectedItem();
		String rightJcomboboxText = (String) this.jcomboboxRight.getSelectedItem();
		EventInstance leftEvent = null;
		EventInstance rightEvent = null;
		String idLeft, idRight;

		if (leftJcomboboxText.matches("^\\d$")) {
			leftEvent = this.getEventInstanceById((String) this.jcomboboxLeft.getSelectedItem());
		} else {
			leftEvent = new EventInstance();
			leftEvent.setId(leftJcomboboxText);
			leftEvent.setParameters(null);
		}
		if (rightJcomboboxText.matches("^\\d$")) {
			rightEvent = this.getEventInstanceById((String) this.jcomboboxRight.getSelectedItem());
		} else {
			rightEvent = new EventInstance();
			rightEvent.setId(rightJcomboboxText);
			rightEvent.setParameters(null);
		}

		String logicalConnector = this.getLogicalConnector();
		String constraintId = leftJcomboboxText + getSymbolLogicalConnector(logicalConnector) + rightJcomboboxText;
		Constraint c = new Constraint(constraintId, leftEvent, rightEvent, logicalConnector);
		Object[] obj = { c.getId(), leftJcomboboxText, logicalConnector, rightJcomboboxText };
		((DefaultTableModel) this.tblConstraints.getModel()).addRow(obj);
		this.temporaryConstraints.add(c);
	}

	private void confirm() {
		this.values = this.temporaryConstraints;
		this.graphProject.setConstraints(this.values);
		dispose();
	}

	private void cancel() {
		this.getValues().clear();
		this.dispose();
	}

	private void clean() {
		this.jcomboboxLeft.setSelectedIndex(0);
		this.jcomboboxRight.setSelectedIndex(0);
	}

	private void deleteRowTable() {
		int row = this.tblConstraints.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(null, "You must select a row to delete.", "Warning",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// LINHA PRESENTE NO ARRAY CONSTRAINT
		if (!this.temporaryConstraints.isEmpty() && this.temporaryConstraints != null) {
			String constraintId = (String) this.tblConstraints.getValueAt(row, 0);
			Constraint targetConstraint = new Constraint();
			for (Constraint c : this.temporaryConstraints) {
				if (c.getId().equals(constraintId)) {
					targetConstraint = c;
				}
			}
			this.temporaryConstraints.remove(targetConstraint);
		}
		((DefaultTableModel) this.tblConstraints.getModel()).removeRow(row);
	}

	public ArrayList<Constraint> getValues() {
		if (this.values == null) {
			this.values = new ArrayList<Constraint>();
		}
		return this.values;
	}
}
