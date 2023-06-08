 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.com;

import java.awt.Color;
import java.awt.FontMetrics;
import javax.swing.JTextField;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.*;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class PasswordField extends JPasswordField{
    // classe de design de text feild 
    private final Animator  animator;
    private boolean animateHintext=true;
    private float location;
    private boolean show;
    private String labelText="LabelText";
    private Color lineColor=new Color(3,155,216);
    
    private boolean mouseOver = false;

    public String getLabelText() {
        return labelText;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
    
    
    
    public PasswordField(){
        
    setBorder(new EmptyBorder(20,3,10,3));
    setSelectionColor(new Color(76,204,255));// mli ka selecioner kolchi kydar had color
    addMouseListener(new MouseAdapter(){
        @Override
        public void mouseEntered(MouseEvent e) {
            mouseOver=true;
             repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mouseOver=false;
            repaint();
        }
    });
    addFocusListener(new FocusAdapter(){
        @Override
        public void focusGained(FocusEvent e) {
            showing(false);
        }

        @Override
        public void focusLost(FocusEvent e) {
            showing(true);
       }
        
    });
    TimingTarget target= new TimingTargetAdapter(){
        @Override
        public void begin() {
            animateHintext=String.valueOf(getPassword()).equals("");
        }

        @Override
        public void timingEvent(float fraction) {
            location=fraction;
            repaint();
        }
        
    };
       animator = new Animator(300,target);
       animator.setResolution(0);
       animator.setAcceleration(0.5f);
       animator.setDeceleration(0.5f);
    }
    private void showing(boolean action){
        if(animator.isRunning()){
            animator.stop();
        }else{
            location =1;
        }
        animator.setStartFraction(1f-location);
        show=action;
        location=1f-location;
        animator.start();
    }
    @Override
    public void paint(Graphics grphcs){
        super.paint(grphcs);
        Graphics2D g2= (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        int width =getWidth();
        int height = getHeight();
        if(mouseOver){
            g2.setColor(lineColor);
        }else{
            g2.setColor(new Color(150,150,150));
        }
        g2.fillRect(2,height - 1,width - 4,1);
        createHintText(g2);
        createLineStyle(g2);
        g2.dispose();
      }
        
    private void createHintText(Graphics2D g2 ){
      Insets in =   getInsets();
      g2.setColor(new Color(150,150,150));
      FontMetrics ft=g2.getFontMetrics();
      Rectangle2D r2= ft.getStringBounds(labelText, g2);
      double height =getHeight()- in.top - in.bottom;
      double textY=(height-r2.getHeight())/2;
      double size;
      if(animateHintext){
          if(show){
              size=18*(1-location);
          }else{
              size=18*location;
          }
      }else{
          size=18;
      }
      g2.drawString(labelText, in.right, (int) (in.top + textY + ft.getAscent()-size));
    }
    private void createLineStyle(Graphics2D g2){
        if(isFocusOwner()){
            double width=getWidth()-4;
            int height=getHeight();
            g2.setColor(lineColor);
            double size;
            if(show){
                size=width*(1-location);
            }else{
                size=width*location;
            }
            double x=(width-size)/2;
            g2.fillRect((int) (x+2), height-2, (int) size, 2);// bdal x+2 b x*2 animation okhra
        }
    }
     @Override
     public void setText(String string) {
         if(!String.valueOf(getPassword()).equals(string)){
             showing(string.equals(""));
         }
        super.setText(string);
        }

    
}
