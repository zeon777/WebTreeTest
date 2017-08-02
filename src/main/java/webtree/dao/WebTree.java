package webtree.dao;

import webtree.model.TreeEntity;

import java.util.List;


public interface WebTree {

    public boolean deleteNode(int id);
    public TreeEntity SaveNode(TreeEntity node);
    public List getChieldArray(int number);
    public TreeEntity getEntityById(int id);
}
