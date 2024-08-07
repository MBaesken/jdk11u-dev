/*
 * Copyright (c) 1998, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import java.awt.Frame;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/*
 * @test
 * @summary test for defaultCloseOperation property for Swing JFrame and JDialog
 * @library /java/awt/regtesthelpers
 * @build PassFailJFrame
 * @run main/manual DefaultCloseOperation
 */

public class DefaultCloseOperation extends JPanel {

    private static final String INSTRUCTIONS = "Do the following steps:\n\n" +
         "-  Click the \"Open Frame\" button (a TestFrame will appear)\n" +
         "-  On the TestFrame, select \"Close\" from the system menu (the window should go away)\n" +
         "-  Select \"Do Nothing\" from the \"JFrame Default Close Operation\" ComboBox\n" +
         "-  Click the \"Open Frame\" button\n" +
         "-  On the TestFrame, select \"Close\" from the system menu (the window should remain open)\n" +
         "-  Select \"Dispose\" from the \"JFrame Default Close Operation\" ComboBox\n" +
         "-  On the TestFrame, select \"Close\" from the system menu (the window should go away)\n\n\n" +
         "-  Click the \"Open Frame\" button\n" +
         "-  Click the \"Open Dialog\" button (a TestDialog will appear)\n" +
         "-  On the TestDialog, select \"Close\" from the system menu (the window should go away)\n" +
         "-  Select \"Do Nothing\" from the \"JDialog Default Close Operation\" ComboBox\n" +
         "-  Click the \"Open Dialog\" button\n" +
         "-  On the TestDialog, select \"Close\" from the system menu (the window should remain open)\n" +
         "-  Select \"Dispose\" from the \"JDialog Default Close Operation\" ComboBox\n" +
         "-  On the TestDialog, select \"Close\" from the system menu (the window should go away)";

    JComboBox<String> frameCloseOp;

    CloseOpDialog testDialog;
    JComboBox<String> dialogCloseOp;

    public static void main(String[] args) throws Exception {

        PassFailJFrame passFailJFrame = new PassFailJFrame.Builder()
                .title("DefaultCloseOperation Manual Test")
                .instructions(INSTRUCTIONS)
                .testTimeOut(5)
                .rows(20)
                .columns(70)
                .build();

        SwingUtilities.invokeAndWait(() -> {
            DefaultCloseOperation dco = new DefaultCloseOperation();
            dco.init();

            JFrame frame = new JFrame("DefaultCloseOperation");
            frame.add(dco);
            frame.setSize(500,200);

            PassFailJFrame.addTestWindow(frame);
            PassFailJFrame
                    .positionTestWindow(frame, PassFailJFrame.Position.HORIZONTAL);

            frame.setVisible(true);
        });

        passFailJFrame.awaitAndCheck();
    }

    public void init() {
        setLayout(new FlowLayout());

        CloseOpFrame testFrame = new CloseOpFrame();
        testFrame.setLocationRelativeTo(null);
        PassFailJFrame.addTestWindow(testFrame);

        add(new JLabel("JFrame Default Close Operation:"));
        frameCloseOp = new JComboBox<>();
        frameCloseOp.addItem("Hide");
        frameCloseOp.addItem("Do Nothing");
        frameCloseOp.addItem("Dispose");
        frameCloseOp.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = (String)e.getItem();
                switch (item) {
                    case "Do Nothing":
                        testFrame
                            .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                        break;
                    case "Hide":
                        testFrame
                            .setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                        break;
                    case "Dispose":
                        testFrame
                            .setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        break;
                }
            }
        });
        add(frameCloseOp);

        JButton b = new JButton("Open Frame...");
        b.addActionListener(e -> testFrame.setVisible(true));
        add(b);

        testDialog = new CloseOpDialog(testFrame);
        testDialog.setLocationRelativeTo(null);
        PassFailJFrame.addTestWindow(testDialog);

        add(new JLabel("JDialog Default Close Operation:"));
        dialogCloseOp = new JComboBox<>();
        dialogCloseOp.addItem("Hide");
        dialogCloseOp.addItem("Do Nothing");
        dialogCloseOp.addItem("Dispose");
        dialogCloseOp.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = (String)e.getItem();
                switch (item) {
                    case "Do Nothing":
                        testDialog
                            .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                        break;
                    case "Hide":
                        testDialog
                            .setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                        break;
                    case "Dispose":
                        testDialog
                            .setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        break;
                }
            }
        });
        add(dialogCloseOp);

        b = new JButton("Open Dialog...");
        b.addActionListener(e -> testDialog.setVisible(true));
        add(b);
    }

    public static void verifyCloseOperation(Window window, int op) {
        switch (op) {
            case WindowConstants.DO_NOTHING_ON_CLOSE:
                if (!window.isVisible()) {
                    PassFailJFrame
                            .forceFail("defaultCloseOperation=DoNothing failed");
                }
                break;
            case WindowConstants.HIDE_ON_CLOSE:
                if (window.isVisible()) {
                    PassFailJFrame
                            .forceFail("defaultCloseOperation=Hide failed");
                }
                break;
            case WindowConstants.DISPOSE_ON_CLOSE:
                if (window.isVisible() || window.isDisplayable()) {
                    PassFailJFrame
                            .forceFail("defaultCloseOperation=Dispose failed");
                }
                break;
        }
    }
}

class CloseOpFrame extends JFrame {

    public CloseOpFrame() {
        super("DefaultCloseOperation Test");
        getContentPane().add("Center", new JLabel("Test Frame"));
        pack();
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);

        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            DefaultCloseOperation
                    .verifyCloseOperation(this, getDefaultCloseOperation());
        }
    }
}

class CloseOpDialog extends JDialog {

    public CloseOpDialog(Frame owner) {
        super(owner, "DefaultCloseOperation Test Dialog");
        getContentPane().add("Center", new JLabel("Test Dialog"));
        pack();
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);

        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            DefaultCloseOperation
                    .verifyCloseOperation(this, getDefaultCloseOperation());
        }
    }
}
