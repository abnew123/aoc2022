package aoc2022;
class X{String[]g;int R,C,x;
int h(int u,int v){int m=R-2,k=C-2;return g[1+(x/C+u%m+m-2)%m].charAt(1+(x%C+v%k+k-1)%k);}
String s(boolean p,String[]I){
g=I;R=g.length;C=g[0].length();
int a=g[0].indexOf(46)+C,b=R*C+g[R-1].indexOf(46);
return""+f(a,b,p?0:f(b,a,f(a,b,0)));
}
int f(int a,int b,int t){
var q=new int[2*R*C];
for(q[a]=1;q[b]<1;)
for(t++,x=R*C+C;x-->C;)q[x]=q[x]*2|(g[x/C-1].charAt(x%C)>35&(x/C%~-R==1|h(t,0)!=94&h(-t,0)<95&h(0,t)!=60&h(0,-t)!=62)?(q[x+1]|q[x+C])>>1|q[x]|q[x-1]|q[x-C]:0)&1;
return t;
}
}
