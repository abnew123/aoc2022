package aoc2022;
class V{
String[]g;int R,x,y,d,n;
String s(boolean p,String[]I){
for(g=I;!I[R].isEmpty();R++);
y=g[0].indexOf(46);
for(int C:(I[R+1]+"LLLL").toCharArray())if(C<58)n=n*10+C-48;else{
for(;n>0;n--){
int c=d,X=(2-d)%2,Y=(1-d)%2,u=x+X,w=y+Y;
if(p)for(;G(u,w)<1;u+=X,w+=Y);
else if(G(u,w)<1){
int k=(d%2>0?y:x)%50,z="!!!!!!P*d9!H!!!!A!1!!!!!!!J%_>!!!!!!F,,!".charAt(x/50*12+y/50*4+d)-33,b=z%10;c=z/10%4;
k=z==11&d<2?49:z>39?49-k:k;
u=b/3*50+(c%2>0?c/2*49:k);w=b%3*50+(c%2<1?c/2*49:k);
}
if(G(u,w)>3){x=u;y=w;d=c;}
}
d=d-C/3&3;
}
return 1000*M(x)+4*M(y)+d+1004+"";
}
int M(int v){return(v%200+200)%200;}
int G(int x,int y){
return(x=M(x))<R&&(y=M(y))<g[x].length()?g[x].charAt(y)&31:0;
}
}
