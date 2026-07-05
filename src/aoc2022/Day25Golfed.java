package aoc2022;
class Y{
String s(boolean p,String in){
long a=0;
for(String l:in.split("\n")){
long v=0;
for(char c:l.toCharArray())v=v*5+"=-012".indexOf(c)-2;
a+=v;
}
String r="";
for(;a>0;a/=5){
int d=(int)(a%5);
r="012=-".charAt(d)+r;
if(d>2)a+=5;
}
return r;
}
}
