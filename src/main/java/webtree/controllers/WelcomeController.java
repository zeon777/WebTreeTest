package webtree.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import webtree.dao.WebTreeImpl;
import webtree.model.TreeEntity;
import java.io.IOException;
import java.util.List;


/**
 * Created by zeon on 10.06.2017.
 */
@Controller
@SessionAttributes("person")
public class WelcomeController {




        @Autowired
        private WebTreeImpl webTreeImpl;


        @RequestMapping("/")
        public String index() {


            return "WEB-INF/hello.jsp";
        }


        @RequestMapping(value = "/ajaxList", method = RequestMethod.GET) //Контроллер для выборки и передачи списка дочерних узлов
        @ResponseBody
        public String ajaxGetList(@RequestParam(required = false) Integer id) {
            List<TreeEntity> records;
            records=webTreeImpl.getChieldArray(id);

            ObjectMapper mapper = new ObjectMapper();
            try {
                return    mapper.writeValueAsString(records);  //Упакуем данные в строку JSON
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        @RequestMapping(value = "/ajaxUpdate", method = RequestMethod.POST)  //Контроллер для переименования узла
        @ResponseBody
        public String ajaxUpdate(@RequestParam(required = false) Integer id,@RequestParam(required = false) String name) {
            if(id!=null)
            {
                TreeEntity node = webTreeImpl.getEntityById(id);
                node.setName(name);
                webTreeImpl.SaveNode(node);
                return "1";}
            else return "0";
        }
        @RequestMapping(value = "/ajaxAddDelete", method = RequestMethod.POST)   //Контроллер добавление|удаления узла
        @ResponseBody
        public String ajaxDelete(@RequestParam(required = false) Integer id,@RequestParam(required = false) String name) {
            if(id!=null && name==null)   //Если имя с клиента не пришло, то значит необходимо удалить узел по ID
            {
                int PnodeID = webTreeImpl.getEntityById(id).getParentId();

                webTreeImpl.deleteNode(id);
              if(webTreeImpl.getChieldArray(PnodeID).size()==0)
              {
                 TreeEntity Pnode= webTreeImpl.getEntityById(PnodeID);
                  Pnode.setHaveChild((byte) 0);
                  webTreeImpl.SaveNode(Pnode);

              }

                return "1";}
            else if(id!= null && name!=null) { //Если имя с клиента пришло, то ID считаем родительским и создаём новый узел

                TreeEntity node = new TreeEntity();
                node.setName(name);
                node.setParentId(id);
                node.setHaveChild((byte) 0);
                if(id!=0)
                {
                    TreeEntity Pnode = webTreeImpl.getEntityById(id);
                    Pnode.setHaveChild((byte) 1);

                    webTreeImpl.SaveNode(Pnode);
                }
                webTreeImpl.SaveNode(node);
                return "1";
            }
            return "0";
        }


    }