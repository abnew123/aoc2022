package aoc2022;class D{String s(boolean p,String[]x){int r=0,A=0,B=0,C=0,D=0;for(var l:x){for(var t:l.split("\\D")){A=B;B=C;C=D;D=new Integer(t);}if(p?(A-C)*(B-D)<1:A<=D&C<=B)r++;}return""+r;}}
