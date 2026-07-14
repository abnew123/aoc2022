package aoc2022;
class V{
String[]g;int R;
String s(boolean p,String[]I){
g=I;for(;!I[R].isEmpty();R++);
int x=0,y=g[0].indexOf(46),d=0,X[]={0,1,0,-1},Y[]={1,0,-1,0};
var q=I[R+1].trim();int l=q.length();
for(int i=0,n=0,C=0;i<=l;i++)if(i<l&&(C=q.charAt(i))>47&C<58)n=n*10+C-48;else{
for(;n-->0;){
int c=d,u=x+X[d],w=y+Y[d];
if(p)for(;G(u,w)<1;u+=X[d],w+=Y[d]);
else if(G(u,w)<1){var r=H(x,y,d);c=r[0];u=r[1];w=r[2];}
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
return x<R&&y<g[x].length()?g[x].charAt(y)&31:0;
}
int[]H(int x,int y,int d){
int i="6789;@BJKLMTUV".indexOf(48+x/50*12+y/50*4+d)*3,a[]={0,49,50,99,100,149,199,x-100,x-50,149-x,x+50,y-100,y-50,y+50,y+100,y};
var s="09?0>02932<336;31:1480920=22952>1357107107";
return new int[]{s.charAt(i)-48,a[s.charAt(i+1)-48],a[s.charAt(i+2)-48]};
}
}
