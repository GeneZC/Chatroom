package chatroom;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;  

public class Server {  

    String server = "1";
    String pass = "1";
    private JFrame frame;
    private boolean isMoved;  
    private Point pre_point;  
    private Point end_point;
    private JTextField serverText;
	private JPasswordField passwordText;
	private ImagePanel mainpanel;
	Server(){
	}
	
    public static void main(String[] args){  
        new Server().run();  
    }  
    
    private void setDragable(final JFrame l) {  
        this.frame.addMouseListener(new java.awt.event.MouseAdapter() {  
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
       this.frame.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {  
            public void mouseDragged(java.awt.event.MouseEvent e) {  
                if (isMoved) {// 判断是否可以拖拽  
                    end_point = new Point(l.getLocation().x + e.getX() - pre_point.x,  
                            l.getLocation().y + e.getY() - pre_point.y);  
                    l.setLocation(end_point);  
                }  
            }  
        });  
    }   
    
    public void setServer()
    {
    	Thread t = new Thread(new ServerUI());
    	t.start();
    }
    
    public class LoginButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent e) {
			if(serverText.getText().trim().length()==0||new String(passwordText.getPassword()).trim().length()==0){
				JOptionPane.showMessageDialog(null, "INVALIDATE!!");
				return;
			}
			if(serverText.getText().trim().equals(server)&&new String(passwordText.getPassword()).trim().equals(pass)){
				JOptionPane.showMessageDialog(null, "SUCCESS!!");
				frame.dispose();
				frame.remove(mainpanel);
				setServer();
				//frame = null;
				
			 }
			else{
				JOptionPane.showMessageDialog(null, "WRONG!!");
			}
		}
    }
    
    public class ExitButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			System.exit(0);
		}
	}
    
    public void run(){  
    	//JFrame.setDefaultLookAndFeelDecorated(true); 
    	
        frame = new JFrame();//font?
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
    	frame.setUndecorated(true);
    	frame.setLocation(500, 500);   
    	frame.setSize(370, 240);
    	frame.setResizable(false);
    	frame.setOpacity(0.7f);
        this.setDragable(frame);//vibration?
    	
        Image img = new ImageIcon("Resource/test.jpg").getImage();
        mainpanel = new ImagePanel(img);
        //imagepanel.setLayout(null);
        //imagepanel.setOpaque(false);
    	//JPanel mainpanel = new JPanel();
    	mainpanel.setLayout(null);
    	mainpanel.setOpaque(false);
    	
    	JLabel title = new JLabel("Server");
    	title.setBounds(20, 0, 70, 30);
    	mainpanel.add(title);
    	JLabel serverName = new JLabel("Server: ");
    	serverName.setBounds(100, 40, 200, 100);
    	mainpanel.add(serverName);
    	serverText = new JTextField(17);
    	serverText.setBounds(150, 70, 150, 40);
    	mainpanel.add(serverText);
    	JLabel password = new JLabel("Password: ");
    	password.setBounds(80, 90, 200, 100);
    	mainpanel.add(password);
    	passwordText = new JPasswordField();
    	passwordText.setBounds(150, 120, 150, 40);
    	mainpanel.add(passwordText);
    	JButton login = new JButton("Login");
    	login.setBounds(220, 170, 70, 30);
    	login.addActionListener(new LoginButtonListener());
    	mainpanel.add(login);
    	JButton exit = new JButton("Exit");
    	exit.setBounds(220, 200, 70, 30);
    	exit.addActionListener(new ExitButtonListener());
    	mainpanel.add(exit);
    	

    	//frame.add(imagepanel);
    	frame.add(mainpanel);
    	//frame.show();
    	frame.setVisible(true);
    }
    
    public class ServerUI implements Runnable{
    	ArrayList<PrintWriter> clientOutputStreams;
        private JTextArea incoming;
        private boolean _isMoved;  
        private Point _pre_point;  
        private Point _end_point;
        private JFrame _frame;
        
        @Override
        public void run() 
        {
        	// build GUI  
            _frame = new JFrame(); 
            //_frame.setLayout(null);
            _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        	_frame.setUndecorated(true);
        	_frame.setLocation(500, 100);   
        	_frame.setSize(500, 500);
        	_frame.setResizable(false);
        	_frame.setOpacity(0.7f);
        	this.setDragable(_frame);//vibration?
        	//JLabel test = new JLabel("test");
        	//frame.add(test);
        	
            Image img = new ImageIcon("Resource/test1.jpg").getImage();
            ImagePanel _mainpanel = new ImagePanel(img);
        	//JPanel _mainpanel = new JPanel();
        	_mainpanel.setLayout(null);
        	//_mainpanel.setOpaque(false);
            
            incoming = new JTextArea(15,20);    
            incoming.setLineWrap(true);  
            incoming.setWrapStyleWord(true);  
            incoming.setEditable(false);  
            JScrollPane qScroller = new JScrollPane(incoming);  
            qScroller.setBounds(5, 10, 470, 400);
            qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);  
            qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            _mainpanel.add(qScroller);
            //_mainpanel.add(qScroller);
            
            JButton exit = new JButton("Exit");
            exit.setBounds(400, 450, 70, 30);
        	exit.addActionListener(new ExitButtonListener());
        	_mainpanel.add(exit); 
        	//_mainpanel.add(exit); 
            
            //mainpanel.setVisible(true);
            //mainpanel.repaint();
            _frame.add(_mainpanel);
            //_frame.add(_mainpanel);
            //_frame.pack();
            _frame.setVisible(true);
            //frame.repaint();
            
            
            clientOutputStreams = new ArrayList<PrintWriter>();
            try{  
                ServerSocket serverSock = new ServerSocket(10086);  
                while(true){  
                    Socket clientSocket = serverSock.accept();  
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());  
                    clientOutputStreams.add(writer);  
                    Thread t = new Thread(new ClientHandler(clientSocket));  
                    t.start();  
                    System.out.println("got a connection");// 
                    incoming.append("Got a connection.\n");
                }  
            }catch(Exception ex){  
                ex.printStackTrace();  
            }  
        }
        
        private void setDragable(final JFrame l) {  
            _frame.addMouseListener(new java.awt.event.MouseAdapter() {  
                public void mouseReleased(java.awt.event.MouseEvent e) {  
                    _isMoved = false;// 鼠标释放了以后，是不能再拖拽的了  
                    l.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
                }  
      
                public void mousePressed(java.awt.event.MouseEvent e) {  
                    _isMoved = true;  
                    _pre_point = new Point(e.getX(), e.getY());// 得到按下去的位置  
                    l.setCursor(new Cursor(Cursor.MOVE_CURSOR));  
                }  
            });  
            //拖动时当前的坐标减去鼠标按下去时的坐标，就是界面所要移动的向量。  
           _frame.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {  
                public void mouseDragged(java.awt.event.MouseEvent e) {  
                    if (_isMoved) {// 判断是否可以拖拽  
                        _end_point = new Point(l.getLocation().x + e.getX() - _pre_point.x,  
                                l.getLocation().y + e.getY() - _pre_point.y);  
                        l.setLocation(_end_point);  
                    }  
                }  
            });  
        }   
        
        public class ExitButtonListener implements ActionListener{
    		public void actionPerformed(ActionEvent e) {
    			_frame.dispose();
    			System.exit(0);
    		}
    	}
        
        public class ClientHandler implements Runnable{  
            BufferedReader reader;  
            Socket sock;  
            public ClientHandler(Socket clientSocket){  
                try{  
                    sock = clientSocket;  
                    InputStreamReader isReader = new InputStreamReader(sock.getInputStream());  
                    reader = new BufferedReader(isReader);  
                }catch(Exception ex){  
                    ex.printStackTrace();  
                }  
            }  
            @Override  
            public void run() {  
                String message;  
                try{  
                    while((message = reader.readLine()) != null){ 
                    	
                        incoming.append(message + "\n");
                        tellEveryone(message);  //
                    }  
                }catch(Exception ex){  
                    ex.printStackTrace();  
                }  
            }  
        }  
          
        public void tellEveryone(String message){  
            Iterator<PrintWriter> it = clientOutputStreams.iterator();  
            while(it.hasNext()){  
                try{  
                    PrintWriter writer = (PrintWriter)it.next();  
                    writer.println(message);//  
                    writer.flush();//  
                    //incoming.append(message + "\n");
                }catch(Exception ex){  
                    ex.printStackTrace();  
                }  
            }  
        } 
    }
}  