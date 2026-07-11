package aoc2022;
import java.util.*;
class Q{
int[][]P={{15},{2,7,2},{7,4,4},{1,1,1,1},{3,3}};
String s(boolean p,String[]j){
var J=j[0];
List<Integer>c=$.a();Map<String,long[]>m=$.h();
int[]h=new int[7];long n=p?2022:1000000000000L,r=0,e=0;int q=0;var z=0>1;
for(;r<n;r++){
int k=(int)(r%5),x=2,y=c.size()+3;
for(;;){
int X=x+(J.charAt(q)==60?-1:1);q=++q%J.length();
if(o(P[k],X,y,c))x=X;
if(o(P[k],x,y-1,c))y--;else{u(P[k],x,y,c,h);break;}
}
if(!z){
var K=k(r+1,q,c,h);var v=m.putIfAbsent(K,new long[]{r+1,c.size()});
if(v!=null){long a=r+1-v[0],b=c.size()-v[1],g=(n-r-1)/a;r+=g*a;e+=g*b;z=1>0;}
}
}
return""+(e+c.size());
}
boolean o(int[]p,int x,int y,List<Integer>c){
if(x<0||y<0)return 0>1;
for(int r=0;r<p.length;r++){
int w=p[r]<<x;
if((w&-128)!=0||y+r<c.size()&&(w&c.get(y+r))!=0)return 0>1;
}
return 1>0;
}
void u(int[]p,int x,int y,List<Integer>c,int[]h){
for(int r=0;r<p.length;r++){
for(;c.size()<=y+r;)c.add(0);
int w=p[r]<<x;c.set(y+r,c.get(y+r)|w);
for(int i=0;i<7;i++)if((w&1<<i)!=0)h[i]=$.x(h[i],y+r+1);
}
}
String k(long r,int q,List<Integer>c,int[]h){
int f=c.size();var s=r%5+","+q;
for(int x:h){f=$.n(f,x);s+=","+(c.size()-x);}
for(int y=f;y<c.size();y++)s+=","+c.get(y);
return s;
}
}
