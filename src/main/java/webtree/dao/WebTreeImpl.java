package webtree.dao;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Required;
import webtree.model.TreeEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


/**
 * Created by zeon on 23.07.2017.
 */

//Класс работы с БД
@Service
@Transactional
public class WebTreeImpl implements WebTree {


private static WebTreeImpl webTree;


   private WebTreeImpl(){}

    public static synchronized WebTreeImpl getWebTreeImpl()
    {
        if(webTree==null)
            webTree=new WebTreeImpl();

            return webTree;
    }


    @Required
    public TreeEntity getEntityById(int id) {
        TreeEntity root;
        try (Session session = Factory.getSession())
        {
            root= session.get(TreeEntity.class,id);
        }

        return root;
    }

    @Required
    public List<TreeEntity> getChieldArray(int parent) //Получение списка узлов детей parent
    {

        try( Session session=Factory.getSession()) {
            List<TreeEntity> treeEntitys;
            if(parent!=-1)
            {

                String qstring ="from webtree.model.TreeEntity entiti where entiti.parentId LIKE "+parent;
                treeEntitys= (List<TreeEntity>)  session.createQuery(qstring).list();
            }
            else
            {
                treeEntitys= (List<TreeEntity>)  session.createQuery("from webtree.model.TreeEntity ").list();
            }

            return treeEntitys;
        }

    }

    @Required
    public boolean deleteNode(int id)   //Удаление узла по ID + удаление всех наследников
    {

        TreeEntity treeEntity = getEntityById(id);
        if(treeEntity.getHaveChild()==1)
        {

            List<Integer> list= new ArrayList<>();
            for (TreeEntity entity:getChieldArray(id))
            {list.add(entity.getId());}
            for (Integer integer:list)
            {deleteNode(integer);}
        }
        System.out.println("Удаляем "+id);

       if(id!=0)
       {try( Session session=Factory.getSession()) {
           String temp = session.get(TreeEntity.class,id).getName();
           Transaction tx = session.beginTransaction();
           Query query =  session.createQuery("delete TreeEntity where id = :id");
           query.setParameter("id", id);
           query.executeUpdate();
           tx.commit();}
catch (Exception e)
        {System.out.println(e.getMessage());}}

        return true;


    }

@Required
    public TreeEntity SaveNode(TreeEntity node)  //Сохранение или обновление значения нода в БД
    {

            try (Session session=Factory.getSession()) {

                if(node.getId()==null)
                {
                    Transaction transaction = session.beginTransaction();
                    session.save(node);
                }
                else {
                    Transaction transaction = session.beginTransaction();

                    session.saveOrUpdate(node);

                    session.flush();
                }
            }
            return node;

    }

}
