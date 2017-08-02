package webtree.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * Created by zeon on 01.08.2017.
 */

@Entity
@Table(name = "tree", schema = "webtree", catalog = "")
public class TreeEntity {
    @JsonProperty
    private int id;
    @JsonProperty
    private String name;
    @JsonProperty
    private int parentId;
    @JsonProperty("leaf")
    private Byte haveChild;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Basic
    @Column(name = "parentId")
    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    @Basic
    @Column(name = "haveChild")
    public byte getHaveChild()
    {
        return haveChild;
    }

    public void setHaveChild(byte haveChild) {
        this.haveChild = haveChild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeEntity that = (TreeEntity) o;

        if (id != that.id) return false;
        if (parentId != that.parentId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (haveChild != null ? !haveChild.equals(that.haveChild) : that.haveChild != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + parentId;
        result = 31 * result + (haveChild != null ? haveChild.hashCode() : 0);
        return result;
    }
}
