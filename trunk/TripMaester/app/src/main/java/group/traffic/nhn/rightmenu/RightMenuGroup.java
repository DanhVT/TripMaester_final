package group.traffic.nhn.rightmenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SinhHuynh
 * @Tag Group options in menu: routing algorithms, transportations, languages
 */
public class RightMenuGroup {

  public String text;
  public int icon;
  public final List<String> children = new ArrayList<String>();
  int check_index = 0;
  public boolean check = false;

  public RightMenuGroup(String text) {
    this.text = text;
  }
  
  public void setCheck_index(int index){
	  check_index = index;
  }

} 

