clc;
a = csvread('bfs_maze_shortest.csv');
[nx,ny] = size(a);
a_copy = a;
ind = find(a<1);
a(ind) = -inf;
%  figure(1)
% imagesc(a)
maxval = max(a(:)); %find maximum intensity
% mymap = [0,0,0;0.5,0.5,0.5;0.75,0.75,0.75];
map = colormap(summer); %get current colormap (usually this will be the default one)
a = floor((a./maxval)*length(map)); % linearly rescale the image to map into the colormap: this is what imagesc does

a_copy=ind2rgb(a, map);

[x, y] = ind2sub(size(a_copy),ind);
% now set colors to white
for indx = 1:length(x)
      a_copy(x(indx),y(indx),:) = [0 0 0];
end
% plot the image
% looks like original image except -inf were set to white
% figure(2)
image(a_copy);
axis off;
imagesc(a_copy);
pbaspect([15 15 1]);
set(gca,'xtick', linspace(0.5,15+0.5,15+1), 'ytick', linspace(0.5,15+0.5,15+1));
set(gca,'xgrid', 'on', 'ygrid', 'on', 'gridlinestyle', '-', 'xcolor', 'k',...
'ycolor', 'k','xticklabel',[], 'yticklabel',[]);