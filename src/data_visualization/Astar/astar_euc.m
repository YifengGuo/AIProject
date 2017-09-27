clc;
a = csvread('Astar_euc_maze_shortest.csv');
path = csvread('Astar_euc_path_shortest.csv');
[nx,ny] = size(a);
a_copy = a;
ind = find(a<1);
a(ind) = -inf;
%  figure(1)
% imagesc(a)
for i = 1:1:length(path)
    xp = path(i,1);
    yp = path(i,2);
    a(xp+1,yp+1) = 32;
    maxval = max(a(:)); %find maximum intensity
    % mymap = [0,0,0;0.5,0.5,0.5;0.75,0.75,0.75];
    map = colormap(summer); %get current colormap (usually this will be the default one)
    a = floor((a./maxval)*length(map)); % linearly rescale the image to map into the colormap: this is what imagesc does

    a_copy=ind2rgb(a, map);

    [x, y] = ind2sub(size(a_copy),ind);
    % now set colors to white
    for indx = 1:length(x)
        a_copy(x(indx),y(indx),:) = [0 0 0];
      
        % plot the image
        % looks like original image except -inf were set to white
    	% figure(2)
    	I = image(a_copy);
    	axis off;
    	imagesc(a_copy);
    	pbaspect([15 15 1]);
    	set(gca,'xtick', linspace(0.5,15+0.5,15+1), 'ytick', linspace(0.5,15+0.5,15+1));
    	set(gca,'xgrid', 'on', 'ygrid', 'on', 'gridlinestyle', '-', 'xcolor', 'k',...
    	'ycolor', 'k','xticklabel',[], 'yticklabel',[]);
    % 	imresize(a_copy, 10)
    % 	
    end
    filename = strcat('../../../images/astar/', int2str(i));
    filename = strcat(filename, '.bmp');
    saveas(gcf, filename, 'bmp');
end

% generate gif
for i = 1:1:length(path)
    filename = strcat('../../../images/astar/', int2str(i));
    filename = strcat(filename, '.bmp');
    gifname = '../../../images/astar/path_euc.gif';
    A = imread(filename);
    [I,map]=rgb2ind(A,256);
    if (i==1)
        imwrite(I,map,gifname,'DelayTime',0.1,'LoopCount',Inf)
    else
        imwrite(I,map,gifname,'WriteMode','append','DelayTime',0.1)
    end
end