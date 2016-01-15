package br.uff.ic.provviewer;

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.GUI.GuiButtons;
import br.uff.ic.provviewer.GUI.GuiInitialization;
import br.uff.ic.provviewer.GUI.GuiProlog;
import br.uff.ic.provviewer.GUI.GuiReadFile;
import br.uff.ic.provviewer.GUI.GuiRun;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Prov Viewer GUI. Can be used as a Template.
 * @author kohwalter
 */
public class GraphFrame extends javax.swing.JFrame {

    Variables variables = new Variables();

    /**
     * Creates new form GraphFrame
     * @param graph 
     */
    public GraphFrame(DirectedGraph<Object, Edge> graph) {
        initComponents();
        GuiInitialization.initGraphComponent(variables, graph, this, Layouts);       
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        ToolMenu = new javax.swing.JPanel();
        CollapseAgent = new javax.swing.JButton();
        Reset = new javax.swing.JButton();
        Expand = new javax.swing.JButton();
        Collapse = new javax.swing.JButton();
        MouseModes = new javax.swing.JComboBox();
        FilterNodeAgentButton = new javax.swing.JCheckBox();
        FilterNodeLonelyButton = new javax.swing.JCheckBox();
        DisplayEdges = new javax.swing.JLabel();
        EdgeLineShapeSelection = new javax.swing.JComboBox();
        StatusFilterBox = new javax.swing.JComboBox();
        ShowEdgeTextButton = new javax.swing.JCheckBox();
        AttributeStatus = new javax.swing.JLabel();
        EdgeStyle = new javax.swing.JLabel();
        MouseModeLabel = new javax.swing.JLabel();
        EdgeTypes = new javax.swing.JScrollPane();
        FilterList = new javax.swing.JList();
        Layouts = new javax.swing.JComboBox();
        GraphLayout = new javax.swing.JLabel();
        FilterEdgeAgentButton = new javax.swing.JCheckBox();
        InitPrologButton = new javax.swing.JToggleButton();
        PrologGenerateFacts = new javax.swing.JButton();
        PrologSimilarityInference = new javax.swing.JButton();
        edgeTypeField = new javax.swing.JTextField();
        FilterNodeEntityButton = new javax.swing.JCheckBox();
        FilterVertexMinValue = new javax.swing.JTextField();
        FilterVertexMaxValue = new javax.swing.JTextField();
        TemporalFilterToggle = new javax.swing.JToggleButton();
        MenuBar = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        OpenConfig = new javax.swing.JMenuItem();
        OpenGraph = new javax.swing.JMenuItem();
        Exit = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        autoDetectEdgesCheckBox = new javax.swing.JCheckBoxMenuItem();

        fileChooser.setCurrentDirectory(new java.io.File("D:\\SVN\\Prov_Viewer\\prov-viewer\\src\\main\\resources"));
        fileChooser.setDialogTitle("This is my open dialog");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Prov Viewer");
        setMinimumSize(new java.awt.Dimension(850, 0));

        ToolMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        CollapseAgent.setText("CollapseAgent");
        CollapseAgent.setToolTipText("Collapse all vertices from each agent");
        CollapseAgent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CollapseAgentActionPerformed(evt);
            }
        });

        Reset.setText("Reset");
        Reset.setToolTipText("Reset the graph to the original format");
        Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetActionPerformed(evt);
            }
        });

        Expand.setText("Expand");
        Expand.setToolTipText("Remove the collapse from the selected vertex");
        Expand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExpandActionPerformed(evt);
            }
        });

        Collapse.setText("Collapse");
        Collapse.setToolTipText("Collapse selected vertices");
        Collapse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CollapseActionPerformed(evt);
            }
        });

        MouseModes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Transforming", "Picking" }));
        MouseModes.setToolTipText("Change the mouse function to select (Picking) or moving (Transforming)");
        MouseModes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MouseModesActionPerformed(evt);
            }
        });

        FilterNodeAgentButton.setText("Agents Vertices");
        FilterNodeAgentButton.setToolTipText("Filter all agent vertices");
        FilterNodeAgentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilterNodeAgentButtonActionPerformed(evt);
            }
        });

        FilterNodeLonelyButton.setText("Lonely Vertices");
        FilterNodeLonelyButton.setToolTipText("Filter all vertices without neighbors");
        FilterNodeLonelyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilterNodeLonelyButtonActionPerformed(evt);
            }
        });

        DisplayEdges.setText("Display Edge");

        EdgeLineShapeSelection.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "QuadCurve", "Line" }));
        EdgeLineShapeSelection.setToolTipText("Change the edge's line format");
        EdgeLineShapeSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EdgeLineShapeSelectionActionPerformed(evt);
            }
        });

        StatusFilterBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default", "Morale", "Stamina", "Hours", "Weekend", "Credits", "Role" }));
        StatusFilterBox.setToolTipText("Change vertex coloring to evaluate the selected attribute");
        StatusFilterBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatusFilterBoxActionPerformed(evt);
            }
        });

        ShowEdgeTextButton.setText("Edge Text");
        ShowEdgeTextButton.setToolTipText("Display the Edge's type");
        ShowEdgeTextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowEdgeTextButtonActionPerformed(evt);
            }
        });

        AttributeStatus.setText("Attribute Status");

        EdgeStyle.setText("Edge Style");

        MouseModeLabel.setText("Mouse Mode");

        FilterList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        FilterList.setToolTipText("Edge filter: Display only the edges with selected labels");
        FilterList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                FilterListValueChanged(evt);
            }
        });
        EdgeTypes.setViewportView(FilterList);

        Layouts.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CircleLayout", "FRLayout", "FRLayout2", "TemporalLayout", "SpatialLayout", "ISOMLayout", "KKLayout" }));
        Layouts.setToolTipText("Change the graph layout");
        Layouts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LayoutsActionPerformed(evt);
            }
        });

        GraphLayout.setText("Graph Layout");

        FilterEdgeAgentButton.setSelected(true);
        FilterEdgeAgentButton.setText("Agent Edge");
        FilterEdgeAgentButton.setToolTipText("Filter all edges that connect to an agent");
        FilterEdgeAgentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilterEdgeAgentButtonActionPerformed(evt);
            }
        });

        InitPrologButton.setText("Init Prolog");
        InitPrologButton.setToolTipText("Initialzie the prolog knownledge and facts bases");
        InitPrologButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InitPrologButtonActionPerformed(evt);
            }
        });

        PrologGenerateFacts.setText("Generate Facts");
        PrologGenerateFacts.setToolTipText("Generate the prolog facts from the XML (graph) file");
        PrologGenerateFacts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrologGenerateFactsActionPerformed(evt);
            }
        });

        PrologSimilarityInference.setText("Similarity Collapse");
        PrologSimilarityInference.setToolTipText("Collapse vertices by the Similarity Algorithm using the marked Edge Type below");
        PrologSimilarityInference.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrologSimilarityInferenceActionPerformed(evt);
            }
        });

        edgeTypeField.setText("Neutral");
        edgeTypeField.setToolTipText("Edge type for collapse");
        edgeTypeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edgeTypeFieldActionPerformed(evt);
            }
        });

        FilterNodeEntityButton.setText("Entity Vertices");
        FilterNodeEntityButton.setToolTipText("Filter all entity vertices");

        FilterVertexMinValue.setText("0");
        FilterVertexMinValue.setToolTipText("Filter vertices by time (min value)");
        FilterVertexMinValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilterVertexMinValueActionPerformed(evt);
            }
        });

        FilterVertexMaxValue.setText("100");
        FilterVertexMaxValue.setToolTipText("Filter vertices by time (max value)");
        FilterVertexMaxValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilterVertexMaxValueActionPerformed(evt);
            }
        });

        TemporalFilterToggle.setText("Temporal Filter");
        TemporalFilterToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TemporalFilterToggleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ToolMenuLayout = new javax.swing.GroupLayout(ToolMenu);
        ToolMenu.setLayout(ToolMenuLayout);
        ToolMenuLayout.setHorizontalGroup(
            ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ToolMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(FilterNodeAgentButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ShowEdgeTextButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FilterNodeLonelyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FilterNodeEntityButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(FilterEdgeAgentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(DisplayEdges, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                    .addComponent(EdgeTypes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CollapseAgent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Collapse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Expand, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Reset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PrologSimilarityInference, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PrologGenerateFacts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(InitPrologButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(edgeTypeField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ToolMenuLayout.createSequentialGroup()
                        .addComponent(FilterVertexMinValue, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FilterVertexMaxValue, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                    .addComponent(StatusFilterBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AttributeStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TemporalFilterToggle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(EdgeStyle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(GraphLayout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MouseModeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(MouseModes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Layouts, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EdgeLineShapeSelection, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        ToolMenuLayout.setVerticalGroup(
            ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ToolMenuLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InitPrologButton)
                    .addComponent(CollapseAgent)
                    .addComponent(ShowEdgeTextButton)
                    .addComponent(DisplayEdges)
                    .addComponent(AttributeStatus))
                .addGap(2, 2, 2)
                .addComponent(EdgeTypes, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(ToolMenuLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(FilterNodeAgentButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FilterEdgeAgentButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FilterNodeEntityButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FilterNodeLonelyButton))
            .addGroup(ToolMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MouseModes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MouseModeLabel))
                .addGap(3, 3, 3)
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Collapse)
                    .addComponent(PrologGenerateFacts)
                    .addComponent(StatusFilterBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Layouts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GraphLayout))
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Expand)
                    .addComponent(PrologSimilarityInference)
                    .addComponent(EdgeLineShapeSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EdgeStyle)
                    .addComponent(TemporalFilterToggle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ToolMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Reset)
                    .addComponent(edgeTypeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FilterVertexMinValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FilterVertexMaxValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(ToolMenu, java.awt.BorderLayout.PAGE_END);

        FileMenu.setText("File");

        OpenConfig.setText("Open Config File");
        OpenConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenConfigActionPerformed(evt);
            }
        });
        FileMenu.add(OpenConfig);

        OpenGraph.setText("Open Graph File");
        OpenGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenGraphActionPerformed(evt);
            }
        });
        FileMenu.add(OpenGraph);

        Exit.setText("Exit");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        FileMenu.add(Exit);

        MenuBar.add(FileMenu);

        jMenu1.setText("Options");

        autoDetectEdgesCheckBox.setText("Auto Detect Edges");
        autoDetectEdgesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoDetectEdgesCheckBoxActionPerformed(evt);
            }
        });
        jMenu1.add(autoDetectEdgesCheckBox);

        MenuBar.add(jMenu1);

        setJMenuBar(MenuBar);

        setSize(new java.awt.Dimension(744, 743));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    /**
     * ================================================
     * Expand Button
     * ================================================
     */
    private void ExpandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExpandActionPerformed
        GuiButtons.Expand(variables);
    }//GEN-LAST:event_ExpandActionPerformed
    /**
     * ================================================
     * Collapse Button
     * ================================================
     */
    private void CollapseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CollapseActionPerformed
        GuiButtons.Collapse(variables);
    }//GEN-LAST:event_CollapseActionPerformed
    /**
     * ================================================
     * Reset Button
     * ================================================
     */
    private void ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetActionPerformed
        GuiButtons.Reset(variables);
    }//GEN-LAST:event_ResetActionPerformed
    /**
     * ================================================
     * Select Mouse mode Button
     * ================================================
     */
    private void MouseModesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MouseModesActionPerformed
        GuiButtons.MouseModes(variables.mouse, MouseModes);
    }//GEN-LAST:event_MouseModesActionPerformed
    /**
     * ================================================
     * Collapse Agent's processes
     * ================================================
     */
    private void CollapseAgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CollapseAgentActionPerformed
        GuiButtons.CollapseAgent(variables);
    }//GEN-LAST:event_CollapseAgentActionPerformed

   /**
         * ================================================
         * Filtering agent vertices
         * ================================================
         */
    private void FilterNodeAgentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilterNodeAgentButtonActionPerformed
        GuiButtons.Filter(variables);
    }//GEN-LAST:event_FilterNodeAgentButtonActionPerformed

   /**
         * ================================================
         * Filtering lonely vertices
         * ================================================
         */
    private void FilterNodeLonelyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilterNodeLonelyButtonActionPerformed
        GuiButtons.Filter(variables);
    }//GEN-LAST:event_FilterNodeLonelyButtonActionPerformed

   /**
         * ================================================
         * Edge Shape: Make it to be a line instead of quadratic curves
         * ================================================
         */
    private void EdgeLineShapeSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EdgeLineShapeSelectionActionPerformed
        GuiButtons.EdgeLineMode(EdgeLineShapeSelection, variables);
    }//GEN-LAST:event_EdgeLineShapeSelectionActionPerformed

    /**
         * ================================================
         * Status Filter box
         * ================================================
         */
    private void StatusFilterBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StatusFilterBoxActionPerformed
        GuiButtons.StatusFilter(variables);
    }//GEN-LAST:event_StatusFilterBoxActionPerformed
    /**
         * ================================================
         * Show edge text button
         * ================================================
         */
    private void ShowEdgeTextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowEdgeTextButtonActionPerformed
        GuiButtons.EdgeTextDisplay(variables, ShowEdgeTextButton.isSelected());        
    }//GEN-LAST:event_ShowEdgeTextButtonActionPerformed

   
    private void FilterListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_FilterListValueChanged
        GuiButtons.Filter(variables);
    }//GEN-LAST:event_FilterListValueChanged

    private void LayoutsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LayoutsActionPerformed
        GuiButtons.LayoutSelection(variables, Layouts);
    }//GEN-LAST:event_LayoutsActionPerformed

    private void OpenConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenConfigActionPerformed
        GuiReadFile.openConfigFile(variables, fileChooser, this);
    }//GEN-LAST:event_OpenConfigActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        GuiButtons.Exit();
    }//GEN-LAST:event_ExitActionPerformed

    private void OpenGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenGraphActionPerformed
        GuiReadFile.openGraphFile(variables, fileChooser, this, Layouts);
    }//GEN-LAST:event_OpenGraphActionPerformed

    private void FilterEdgeAgentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilterEdgeAgentButtonActionPerformed
        GuiButtons.Filter(variables);
    }//GEN-LAST:event_FilterEdgeAgentButtonActionPerformed

    private void InitPrologButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InitPrologButtonActionPerformed
        GuiProlog.InitializeProlog(variables.testProlog, variables.prologIsInitialized, InitPrologButton);
    }//GEN-LAST:event_InitPrologButtonActionPerformed

    private void PrologGenerateFactsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrologGenerateFactsActionPerformed
        GuiProlog.GeneratePrologFacts(variables.initialGraph, variables.file, Variables.demo);
    }//GEN-LAST:event_PrologGenerateFactsActionPerformed

    private void PrologSimilarityInferenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrologSimilarityInferenceActionPerformed
        GuiProlog.SimilarityCollapse(InitPrologButton.isSelected(), variables.testProlog, variables, edgeTypeField.getText());
    }//GEN-LAST:event_PrologSimilarityInferenceActionPerformed

    private void edgeTypeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edgeTypeFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edgeTypeFieldActionPerformed

    private void FilterVertexMaxValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilterVertexMaxValueActionPerformed
        // TODO add your handling code here:
        if(TemporalFilterToggle.isSelected())
            GuiButtons.Filter(variables);
    }//GEN-LAST:event_FilterVertexMaxValueActionPerformed

    private void FilterVertexMinValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilterVertexMinValueActionPerformed
        // TODO add your handling code here:
        if(TemporalFilterToggle.isSelected())
            GuiButtons.Filter(variables);
    }//GEN-LAST:event_FilterVertexMinValueActionPerformed

    private void TemporalFilterToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TemporalFilterToggleActionPerformed
        // TODO add your handling code here:
        GuiButtons.Filter(variables);
    }//GEN-LAST:event_TemporalFilterToggleActionPerformed

    private void autoDetectEdgesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoDetectEdgesCheckBoxActionPerformed
        // TODO add your handling code here:
        GuiButtons.AutoDetectEdge(variables, autoDetectEdgesCheckBox.getState());
    }//GEN-LAST:event_autoDetectEdgesCheckBoxActionPerformed
   
    /**
     * Main
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional)">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    
                }
            }
        } catch (ClassNotFoundException ex) {
                Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(GraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        GuiRun.Run();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AttributeStatus;
    private javax.swing.JButton Collapse;
    private javax.swing.JButton CollapseAgent;
    private javax.swing.JLabel DisplayEdges;
    private javax.swing.JComboBox EdgeLineShapeSelection;
    private javax.swing.JLabel EdgeStyle;
    private javax.swing.JScrollPane EdgeTypes;
    private javax.swing.JMenuItem Exit;
    private javax.swing.JButton Expand;
    private javax.swing.JMenu FileMenu;
    public static javax.swing.JCheckBox FilterEdgeAgentButton;
    public static javax.swing.JList FilterList;
    public static javax.swing.JCheckBox FilterNodeAgentButton;
    public static javax.swing.JCheckBox FilterNodeEntityButton;
    public static javax.swing.JCheckBox FilterNodeLonelyButton;
    public static javax.swing.JTextField FilterVertexMaxValue;
    public static javax.swing.JTextField FilterVertexMinValue;
    private javax.swing.JLabel GraphLayout;
    private javax.swing.JToggleButton InitPrologButton;
    private javax.swing.JComboBox Layouts;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JLabel MouseModeLabel;
    private javax.swing.JComboBox MouseModes;
    private javax.swing.JMenuItem OpenConfig;
    private javax.swing.JMenuItem OpenGraph;
    private javax.swing.JButton PrologGenerateFacts;
    private javax.swing.JButton PrologSimilarityInference;
    private javax.swing.JButton Reset;
    private javax.swing.JCheckBox ShowEdgeTextButton;
    public static javax.swing.JComboBox StatusFilterBox;
    public static javax.swing.JToggleButton TemporalFilterToggle;
    private javax.swing.JPanel ToolMenu;
    private javax.swing.JCheckBoxMenuItem autoDetectEdgesCheckBox;
    public javax.swing.JTextField edgeTypeField;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu jMenu1;
    // End of variables declaration//GEN-END:variables
}