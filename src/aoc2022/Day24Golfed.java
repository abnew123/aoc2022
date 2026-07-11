package aoc2022;
import java.util.*;
class X{
String[]g;int R,C;
String s(boolean p,String[]I){
g=I;R=g.length;C=g[0].length();
int a=g[0].indexOf(46),b=(R-1)*C+g[R-1].indexOf(46),t=f(a,b,0);
return""+(p?t:f(a,b,f(b,a,t)));
}
int f(int a,int b,int t){
Set<Integer>q=$.s();q.add(a);
int[]d={0,1,-1,C,-C};
for(;;){
Set<Integer>n=$.s();t++;
for(int p:q)for(int x:d){
int c=p+x;
if(c==b)return t;
if(c>=0&&c<R*C&&w(c,t))n.add(c);
}
q=n;
}
}
boolean w(int p,int t){
int r=p/C,c=p%C;
return g[r].charAt(c)>35&(r<1||r==R-1||g[1+$.f(r-1+t,R-2)].charAt(c)!=94&g[1+$.f(r-1-t,R-2)].charAt(c)!=118&g[r].charAt(1+$.f(c-1+t,C-2))!=60&g[r].charAt(1+$.f(c-1-t,C-2))!=62);
}
}
