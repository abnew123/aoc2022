package aoc2022;
class V{
String[]g;
String s(boolean p,String in){
var v=in.split("\n\n");g=v[0].split("\n");
int x=0,y=g[0].indexOf(46),d=0;int[]X={0,1,0,-1},Y={1,0,-1,0};
var q=v[1].trim();int l=q.length();
for(int i=0,n=0,C=0;i<=l;i++)if(i<l&&(C=q.charAt(i))>47&C<58)n=n*10+C-48;else{
for(;n-->0;){
int c=d,u=x+X[d],w=y+Y[d];
if(p)for(;G(u,w)<1;u+=X[d],w+=Y[d]);
else if(G(u,w)<1){int[]r=H(x,y,d);c=r[0];u=r[1];w=r[2];}
if(G(u,w)==3)break;
x=u;y=w;d=c;
}
x=$.f(x,200);y=$.f(y,200);
if(i<l)d=d+(C==76?3:1)&3;
n=0;
}
return""+(1000*++x+4*++y+d);
}
int G(int x,int y){
x=$.f(x,200);y=$.f(y,200);
return x<g.length&&y<g[x].length()?g[x].charAt(y)&31:0;
}
int[]H(int x,int y,int d){
if(d<1)return x<50?A(2,149-x,99):x<100?A(3,49,x+50):x<150?A(2,149-x,149):A(3,149,x-100);
if(d<2)return x<50?A(2,y-50,99):x<150?A(2,y+100,49):A(1,0,x-100);
if(d<3)return x<50?A(0,149-x,y):x<100?A(1,100,x-50):x<150?A(0,149-x,50):A(1,0,x-100);
if(x<50)return y<100?A(0,y+100,0):A(3,199,y-100);
return A(0,y+50,50);
}
int[]A(int...a){return a;}
}
