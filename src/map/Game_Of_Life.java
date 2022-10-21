package map;

import Logic.Logic;
import service.Service;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game_Of_Life extends JFrame implements ActionListener{

    //主界面
    private JPanel windows;

    //功能条
    private JToolBar Bar;

    //按键:开始、结束
    private JButton GStart,GStop;

    //迭代时长：分、秒
    private JSpinner Minute,Second;

    //游戏地图
    private MyJTextPanel G_Map;

    //字体文本
    SimpleAttributeSet Lattr,Dattr;

    //显示文本
    StyledDocument text;

    // 线程
    private Thread thread;

    Logic logic;
    Service service;

    public static void main(String[] arg){
        new Game_Of_Life();
    }

    public Game_Of_Life(){
        // 调用父类JFrame的构造方法设置窗体标题
        super("生命游戏");

        // 初始化窗体
        initGUI();
    }

    public void initGUI(){
        logic = new Logic();
        service = new Service(5,10);
        Lattr = new SimpleAttributeSet();
        Dattr = new SimpleAttributeSet();
        StyleConstants.setForeground(Lattr,Color.CYAN);
        StyleConstants.setForeground(Dattr,Color.GRAY);

        //背景面板
        windows = new JPanel(new BorderLayout());
        this.setContentPane(windows);

        //中部面板：游戏地图
        G_Map = new MyJTextPanel();
        G_Map.setFont(new Font("思源黑体M",Font.PLAIN,24));
        text = G_Map.getStyledDocument();//UI文本
        windows.add(G_Map,"Center");

        //底部面板：按钮
        Bar = new JToolBar();
        Bar.setFloatable(false);
        windows.add(Bar,"South");

        //功能面板：迭代时间、开始结束
        GStart = new JButton("开始");
        GStop = new JButton("暂停");
        GStart.setEnabled(true);
        GStop.setEnabled(false);
        GStart.setPreferredSize(new Dimension(70,20));
        GStop.setPreferredSize(new Dimension(70,20));

        SpinnerModel M_model = new SpinnerNumberModel(0, 0, 60, 1);
        SpinnerModel S_model = new SpinnerNumberModel(0, 0, 60, 1);
        Minute = new JSpinner(M_model);
        Second = new JSpinner(S_model);

        Bar.add(Minute);
        JLabel Label_minute = new JLabel("分");
        Label_minute.setFont(new Font("思源黑体M",Font.PLAIN,20));
        Label_minute.setBackground(new Color(255,255,255));
        Label_minute.setForeground(new Color(0));
        Bar.add(Label_minute);

        Bar.add(Second);
        JLabel Label_second = new JLabel("秒");
        Label_second.setFont(new Font("思源黑体M",Font.PLAIN,20));
        Label_second.setBackground(new Color(255,255,255));
        Label_second.setForeground(new Color(0));
        Bar.add(Label_second);

        Bar.add(GStart);
        Bar.add(GStop);
        GStart.addActionListener(this);
        GStop.addActionListener(this);

        // 关闭窗口时退出
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗体大小
        this.setSize(270,240);
        // 窗体大小可变
        this.setResizable(false);
        // 窗体居中显示
        this.setLocationRelativeTo(null);
        // 窗体可见
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==GStart){
            if(Integer.parseInt(Minute.getValue().toString())==0 && Integer.parseInt(Second.getValue().toString())==0){
                JOptionPane.showMessageDialog(null, "迭代时间不能为0");
                return;
            }
            service.randomCell(logic);
            logic.setIsRunning(true);
            autoProduce();
            Second.setEnabled(false);
            Minute.setEnabled(false);
            GStart.setEnabled(false);
            GStop.setEnabled(true);
        }else if(e.getSource()==GStop){
            if(GStop.getText().equals("暂停")){
                logic.setIsRunning(false);//暂时停止线程
                GStop.setText("继续");
            }else{
                logic.setIsRunning(true);//恢复线程
                autoProduce();
                GStop.setText("暂停");
            }
        }
    }

    private void ShowCell(){
        //int n = 0;
        try{
            text.remove(0,text.getLength());
        }catch (Exception e){
            e.printStackTrace();
        }
        for(int i = 0;i<5;i++){
            for(int j = 0;j<10;j++){
                try {
                    if (logic.getIsSelected(i, j)) {
                        text.insertString(text.getLength(), "生", Lattr);
                    } else {
                        text.insertString(text.getLength(), "死", Dattr);
                    }
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // 自动繁衍
    public void autoProduce()
    {
        thread = new Thread(new Runnable()
        {
            public void run()
            {
                while (logic.getIsRunning())
                {
                    // 产生下一代
                    service.update(logic);
                    ShowCell();

                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1)
                    {
                        e1.printStackTrace();
                    }

                    logic.setIsDead(true);

                    for(int row = 1; row <= 5; row++)
                    {
                        for (int col = 1; col <= 10; col++)
                        {
                            if (service.getGrid()[row][col] != 0)
                            {
                                logic.setIsDead(false);
                                break;
                            }
                        }
                        if (!logic.getIsDead())
                        {
                            break;
                        }
                    }

                    TimeChange();

                    if (logic.getIsDead()) {
                        JOptionPane.showMessageDialog(null, "所有细胞已死亡");
                        NextTerm();
                    }else if(Integer.parseInt(Minute.getValue().toString())==0 && Integer.parseInt(Second.getValue().toString())==0){
                        JOptionPane.showMessageDialog(null, "迭代完成");
                        NextTerm();
                    }
                }
            }
        });
        thread.start();
    }

    private void NextTerm() {
        logic.setIsRunning(false);
        thread = null;
        GStart.setEnabled(true);
        Minute.setEnabled(true);
        Second.setEnabled(true);
        GStop.setEnabled(false);
    }

    private void TimeChange() {
        if(Integer.parseInt(Second.getValue().toString())!=0){
            Second.setValue(Integer.parseInt(Second.getValue().toString())-1);
        }else{
            Minute.setValue(Integer.parseInt(Minute.getValue().toString())-1);
            Second.setValue(59);
        }
    }
}
