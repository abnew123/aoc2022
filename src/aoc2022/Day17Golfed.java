package aoc2022;
import java.util.*;
class Q{
int[]P={15,132866,263175,16843009,771},h;List<Integer>c;
String s(boolean p,String[]j){
var J=j[0];
c=$.a();var m=$.h();
h=new int[7];long n=p?2022:0xe8d4a51000L,r=0,e=0;int q=0;
for(;r<n;r++){
int k=$.f(r,5),x=2,y=c.size()+3;
for(;;){
int X=x+(J.charAt(q)<61?-1:1);q=++q%J.length();
if(o(P[k],X,y))x=X;
if(o(P[k],x,y-1))y--;else{u(P[k],x,y);break;}
}
if(e<1){
var v=(long[])m.put(k(r+1,q),new long[]{r+1,c.size()});
if(v!=null){long a=r+1-v[0],g=(n-r-1)/a;r+=g*a;e=g*(c.size()-v[1]);}
}
}
return e+c.size()+"";
}
boolean o(int p,int x,int y){
if((x|y)<0)return 0>1;
for(;p>0;p>>=8,y++){
int w=p%256<<x;
if(w>127||y<c.size()&&(w&c.get(y))>0)return 0>1;
}
return 1>0;
}
void u(int p,int x,int y){
for(;p>0;p>>=8,y++){
for(;c.size()<=y;)c.add(0);
int w=p%256<<x;c.set(y,c.get(y)|w);
for(int i=7;i-->0;)if((w&1<<i)>0)h[i]=$.x(h[i],y+1);
}
}
String k(long r,int q){
int f=c.size(),z=f;
for(int x:h)f=f<x?f:x;
return r%5+","+q+c.subList(f,z);
}
}
