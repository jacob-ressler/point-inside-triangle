import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

public class TripointFrame extends JFrame {

	private JPanel contentPane; // Holds all content for the frame
	private JLabel text; // Displays directions and results
	private TripointCanvas canvas; // The canvas
	private JButton resetButton; // Resets the canvas
	private ButtonGroup modeButtons; // Button group for logical and device mode buttons
	private JRadioButton logicalMode, deviceMode; // Changes the displayed coordinate system (logical = Cartesian, device = default)
	private JPanel southContainer; // Container for southTop and southBottom
	private JPanel southBottom; // Container for buttons
	private JPanel southTop; // Container for event log
	private JLabel eventLog; // Tracks the positions of the points
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TripointFrame frame = new TripointFrame();
					frame.pack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TripointFrame() {
		super("Checking if a Point is in a Triangle");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				canvas.repaint();
			}
		});
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(10, 10)); // border layout with 10 hgap and vgap between components
		contentPane.setBackground(Color.decode("#cccccc"));
		setContentPane(contentPane);
		
		text = new JLabel("Select point A of the triangle.");
		text.setSize(contentPane.getWidth(), 25);
		text.setFont(text.getFont().deriveFont(15f)); // font size of 15
		text.setBorder(new EmptyBorder(0, 5, 0, 0));
		contentPane.add(text, BorderLayout.NORTH);
		
		canvas = new TripointCanvas();
		canvas.textReference(text);
		canvas.setSize(600, 600);
		contentPane.add(canvas, BorderLayout.CENTER);
		
		resetButton = new JButton("Reset");
		resetButton.setSize(100, 30);
		resetButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				canvas.clear(); // clear canvas
			}
		});

		logicalMode = new JRadioButton("Logical");
		//logicalMode.setSelected(true);
		logicalMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				canvas.setMode(TripointCanvas.Mode.LOGICAL);
			}
		});
		
		deviceMode = new JRadioButton("Device");
		deviceMode.setSelected(true);
		deviceMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				canvas.setMode(TripointCanvas.Mode.DEVICE);
			}
		});
		
		modeButtons = new ButtonGroup();
		modeButtons.add(deviceMode);
		modeButtons.add(logicalMode);
		
		southContainer = new JPanel();
		
		southBottom = new JPanel();
		southBottom.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		southBottom.add(new JLabel("Displayed Coordinate System:"));
		southBottom.add(deviceMode);
		southBottom.add(logicalMode);
		southBottom.add(resetButton);
		
		southTop = new JPanel();
		southTop.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		eventLog = new JLabel();
		canvas.logReference(eventLog);
		eventLog.setHorizontalTextPosition(JLabel.LEFT);
		eventLog.setFont(eventLog.getFont().deriveFont(Font.ITALIC, 10f));
		southTop.add(eventLog);
		
		southContainer.add(southTop);
		southContainer.add(southBottom);
		
		southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));
		
		contentPane.add(southContainer, BorderLayout.SOUTH);
		
		

	}
}
