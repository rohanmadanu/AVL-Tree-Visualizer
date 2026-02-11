import javax.swing.*;
import java.awt.*;
import java.util.*;

/* ============================================================
   ======================= TREE NODE ===========================
   ============================================================ */
class TreeNode {
    int data;
    int height;
    TreeNode left, right;

    public TreeNode(int data) {
        this.data = data;
        this.height = 1;
    }
}

/* ============================================================
   ========================= AVL BST ===========================
   ============================================================ */
class AVLTree {
    private int getHeight(TreeNode node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(TreeNode node) {
        return (node == null) ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private void updateHeight(TreeNode node) {
        if (node != null) {
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }
    }

    private TreeNode rotateRight(TreeNode y) {
        TreeNode x = y.left;
        TreeNode T2 = x.right;
        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private TreeNode rotateLeft(TreeNode x) {
        TreeNode y = x.right;
        TreeNode T2 = y.left;
        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    public TreeNode insert(TreeNode node, int key) {
        if (node == null) return new TreeNode(key);
        if (key < node.data) node.left = insert(node.left, key);
        else if (key > node.data) node.right = insert(node.right, key);
        else return node; 

        updateHeight(node);
        int balance = getBalance(node);

        if (balance > 1 && key < node.left.data) return rotateRight(node);
        if (balance < -1 && key > node.right.data) return rotateLeft(node);
        if (balance > 1 && key > node.left.data) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && key < node.right.data) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }
}

/* ============================================================
   ======================== DRAW PANEL =========================
   ============================================================ */
class TreePanel extends JPanel {
    private TreeNode root;
    private final int NODE_RADIUS = 25;
    private final int VERTICAL_GAP = 80;

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(10, 15, 30));
        if (root != null) {
            drawTree(g, root, getWidth() / 2, 60, getWidth() / 4);
        }
    }

    private void drawTree(Graphics g, TreeNode node, int x, int y, int hGap) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (node.left != null) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(x, y, x - hGap, y + VERTICAL_GAP);
            drawTree(g, node.left, x - hGap, y + VERTICAL_GAP, hGap / 2);
        }
        if (node.right != null) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(x, y, x + hGap, y + VERTICAL_GAP);
            drawTree(g, node.right, x + hGap, y + VERTICAL_GAP, hGap / 2);
        }

        g2.setColor(new Color(70, 130, 250));
        g2.fillOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
        g2.setColor(Color.WHITE);
        g2.drawOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

        String text = String.valueOf(node.data);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, x - fm.stringWidth(text) / 2, y + fm.getAscent() / 4);
    }
}

/* ============================================================
   ====================== MAIN GUI FRAME =======================
   ============================================================ */
// THIS CLASS NAME MUST MATCH THE FILENAME avlproject2.java
public class avlproject2 extends JFrame {
    private JTextField valuesField;
    private TreePanel treePanel;
    private AVLTree avl = new AVLTree();
    private TreeNode root = null;

    public avlproject2() {
        setTitle("AVL Tree Visualizer");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(30, 35, 50));
        
        JLabel label = new JLabel("Enter Numbers (space separated): ");
        label.setForeground(Color.WHITE);
        
        valuesField = new JTextField(30);
        JButton buildButton = new JButton("Build Tree");
        buildButton.addActionListener(e -> {
            String input = valuesField.getText().trim();
            if (!input.isEmpty()) {
                root = null;
                for (String val : input.split("\\s+")) {
                    root = avl.insert(root, Integer.parseInt(val));
                }
                treePanel.setRoot(root);
                treePanel.repaint();
            }
        });

        topPanel.add(label);
        topPanel.add(valuesField);
        topPanel.add(buildButton);
        add(topPanel, BorderLayout.NORTH);

        treePanel = new TreePanel();
        add(treePanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new avlproject2().setVisible(true));
    }
}