package chatroom;

import java.io.*;  
import java.net.*;  
import javax.swing.*;  
import java.awt.*;  
import java.awt.event.*;  
  
public class Client {  
    JTextArea incoming;  
    JTextField outgoing;  
    BufferedReader reader;  
    PrintWriter writer;  
    Socket sock;  
    static String clientName;
    private JFrame frame;
    private boolean isMoved;  
    private Point pre_point;  
    private Point end_point;
  
    public static void main(String[] args) throws Exception {  
        Client client = new Client();
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			System.out.println("System Style Not Found!");
        }
        clientName = JOptionPane.showInputDialog("Please input the client name :");  
        client.run();  
    }  
  
    private void setDragable(final JFrame l) {  
        frame.addMouseListener(new java.awt.event.MouseAdapter() {  
            public void mouseReleased(java.awt.event.MouseEvent e) {  
                isMoved = false;// 鼠标释放了以后，是不能再拖拽的了  
                l.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
            }  
  
            public void mousePressed(java.awt.event.MouseEvent e) {  
                isMoved = true;  
                pre_point = new Point(e.getX(), e.getY());// 得到按下去的位置  
                l.setCursor(new Cursor(Cursor.MOVE_CURSOR));  
            }  
        });  
        //拖动时当前的坐标减去鼠标按下去时的坐标，就是界面所要移动的向量。  
       frame.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {  
            public void mouseDragged(java.awt.event.MouseEvent e) {  
                if (isMoved) {// 判断是否可以拖拽  
                    end_point = new Point(l.getLocation().x + e.getX() - pre_point.x,  
                            l.getLocation().y + e.getY() - pre_point.y);  
                    l.setLocation(end_point);  
                }  
            }  
        });  
    }  
    
    public class ExitButtonListener implements ActionListener{
    		public void actionPerformed(ActionEvent e) {
    			frame.dispose();
    			System.exit(0);
    		}
    	}

    
    public void run() {  
        // build GUI  
        frame = new JFrame();  
        frame.setUndecorated(true);
        frame.setSize(400, 500);
        frame.setResizable(false);
        frame.setOpacity(0.7f);
        this.setDragable(frame);
        Image img = new ImageIcon("Resource/test.jpg").getImage();
        ImagePanel mainPanel = new ImagePanel(img);  
        mainPanel.setLayout(null);
        
        incoming = new JTextArea(15,20);    
        incoming.setLineWrap(true);  
        incoming.setWrapStyleWord(true);  
        incoming.setEditable(false);  
  
        JScrollPane qScroller = new JScrollPane(incoming);  
        qScroller.setBounds(10, 10, 370, 350);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);  
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);  
  
        outgoing = new JTextField(20);
        outgoing.setBounds(10, 390, 270, 30);
  
        JButton exit = new JButton("Exit");
    	exit.setBounds(300, 420, 70, 30);
    	exit.addActionListener(new ExitButtonListener());

        JButton sendButton = new JButton("Send");  
        sendButton.setBounds(300, 390, 70, 30);
        sendButton.addActionListener(new SendButtonListener());  
  
        JLabel title = new JLabel(clientName + "'s Client");
        title.setBounds(0, 10, 10, 10);
        
    	mainPanel.add(exit);
        mainPanel.add(qScroller);
        mainPanel.add(outgoing);  
        mainPanel.add(sendButton);  
  
        setUpNetworking();  
        Thread readerThread = new Thread(new IncomingReader());  
        readerThread.start();  
  
        //frame.getContentPane().add(mainPanel);
        frame.add(mainPanel);
        frame.setVisible(true);  
  
    } // close go  
  
    private void setUpNetworking() {   
        try {  
            sock = new Socket("127.0.0.1", 10086);  
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());  
            reader = new BufferedReader(streamReader);  
  
            writer = new PrintWriter(sock.getOutputStream());  
  
            System.out.println("networking established");  
        } catch(IOException ex) {  
            ex.printStackTrace();  
        }  
    } // close setUpNetworking    
  
    public class SendButtonListener implements ActionListener {  
        public void actionPerformed(ActionEvent ev) {  
            try {  
                writer.println(clientName+" says:\n"+outgoing.getText());  
                writer.flush();  
            } catch(Exception ex) {  
                ex.printStackTrace();  
            }  
            outgoing.setText("");  
            outgoing.requestFocus();  
        }  
    }  // close SendButtonListener inner class  
  
    public class IncomingReader implements Runnable {  
        public void run() {  
            String message = null;              
            try {  
                while ((message = reader.readLine()) != null) {                         
                    System.out.println("read " + message);  
                    incoming.append(message + "\n");  
                } // close while  
            } catch(Exception ex) {ex.printStackTrace();}  
        } // close run  
    } // close inner class      
}  