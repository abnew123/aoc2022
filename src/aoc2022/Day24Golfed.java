package aoc2022;
class X{
String[]g;int R,C;
String s(boolean p,String[]I){
g=I;R=g.length;C=g[0].length();
int a=g[0].indexOf(46),b=R*C-C+g[R-1].indexOf(46);return""+f(a,b,p?0:f(b,a,f(a,b,0)));
}
int f(int a,int b,int t){
var q=$.s();for(q.add(a);;){
var n=$.s();t++;
for(var p:q)for(int x:new int[]{0,1,-1,C,-C}){
x+=(int)p;
if(x==b)return t;
if(x>=0&x<R*C){int r=x/C,z=x%C;if(g[r].charAt(z)>35&(r%~-R<1|g[1+$.f(r-1+t,R-2)].charAt(z)!=94&g[1+$.f(r-1-t,R-2)].charAt(z)<118&g[r].charAt(1+$.f(z-1+t,C-2))!=60&g[r].charAt(1+$.f(z-1-t,C-2))!=62))n.add(x);}
}
q=n;
}
}
}
