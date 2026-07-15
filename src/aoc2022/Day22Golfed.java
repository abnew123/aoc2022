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
int k=(d&1)>0?y%50:x%50,z="!!!!!!P*d9!H!!!!A!1!!!!!!!J%_>!!!!!!F,,!".charAt(4*(x/50*3+y/50)+d)-33,c=z/10%4,b=z%10;
if(z>39)k=49-k;
if(z==11&d<2)k=49;
return A(c,50*(b/3)+((c&1)>0?(c<2?0:49):k),50*(b%3)+((c&1)<1?(c<1?0:49):k));
}
int[]A(int...a){return a;}
}
