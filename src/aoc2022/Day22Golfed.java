package aoc2022;
class V{
String[]g;int R,x,y,d;
String s(boolean p,String[]I){
for(g=I;!I[R].isEmpty();R++);
y=g[0].indexOf(46);
var q=I[R+1]+"LLLL";
int n=0;for(int C:q.toCharArray())if(C<58)n=n*10+C-48;else{
for(;n-->0;){
int c=d,X=d%2*(2-d),Y=(~d&1)*(1-d),u=x+X,w=y+Y;
if(p)for(;G(u,w)<1;u+=X,w+=Y);
else if(G(u,w)<1){
int k=d%2>0?y%50:x%50,z="!!!!!!P*d9!H!!!!A!1!!!!!!!J%_>!!!!!!F,,!".charAt(4*(x/50*3+y/50)+d)-33,b=z%10;c=z/10%4;
k=z==11&d<2?49:z>39?49-k:k;
u=50*(b/3)+(c%2>0?(c<2?0:49):k);w=50*(b%3)+(c%2<1?(c<1?0:49):k);
}
if(G(u,w)<4)break;
x=u;y=w;d=c;
}
x=(Math.floorMod(x,200));y=(Math.floorMod(y,200));
d=d-C/3&3;
n=0;
}
return 1000*++x+4*++y+d+"";
}
int G(int x,int y){
return(x=(Math.floorMod(x,200)))<R&&(y=(Math.floorMod(y,200)))<g[x].length()?g[x].charAt(y)&31:0;
}
}
