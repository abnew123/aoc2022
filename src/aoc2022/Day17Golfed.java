package aoc2022;
import java.util.*;
class Q{
int[]P={15,132866,263175,16843009,771};
String s(boolean p,String[]j){
var J=j[0];
List<Integer>c=$.a();Map<String,long[]>m=$.h();
int[]h=new int[7];long n=p?2022:1000000000000L,r=0,e=0;int q=0;
for(;r<n;r++){
int k=(int)(r%5),x=2,y=c.size()+3;
for(;;){
int X=x+(J.charAt(q)<61?-1:1);q=++q%J.length();
if(o(P[k],X,y,c))x=X;
if(o(P[k],x,y-1,c))y--;else{u(P[k],x,y,c,h);break;}
}
if(e<1){
var K=k(r+1,q,c,h);var v=m.putIfAbsent(K,new long[]{r+1,c.size()});
if(v!=null){long a=r+1-v[0],b=c.size()-v[1],g=(n-r-1)/a;r+=g*a;e+=g*b;}
}
}
return""+(e+c.size());
}
boolean o(int p,int x,int y,List<Integer>c){
if(x<0|y<0)return 0>1;
for(;p>0;p>>=8,y++){
int w=p%256<<x;
if(w>127||y<c.size()&&(w&c.get(y))>0)return 0>1;
}
return 1>0;
}
void u(int p,int x,int y,List<Integer>c,int[]h){
for(;p>0;p>>=8,y++){
for(;c.size()<=y;)c.add(0);
int w=p%256<<x;c.set(y,c.get(y)|w);
for(int i=0;i<7;i++)if((w&1<<i)>0)h[i]=$.x(h[i],y+1);
}
}
String k(long r,int q,List<Integer>c,int[]h){
int f=c.size(),z=f;var s=r%5+","+q;
for(int x:h){f=$.n(f,x);s+=","+(z-x);}
for(int y=f;y<z;y++)s+=","+c.get(y);
return s;
}
}
