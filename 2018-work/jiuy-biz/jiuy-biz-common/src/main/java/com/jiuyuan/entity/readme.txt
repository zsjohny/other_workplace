规范：
1. 如果一个实体类的某个字段，在任何情况下都不参与json序列化，标记为@JsonIgnore。

========================================================================================================================

model并不仅仅包括实体类，它其实指所有的业务逻辑。
为避免歧义，包名使用entity，意指它仅包含实体类（和一些ValueObject）。

以下内容引用自 http://en.wikipedia.org/wiki/Domain_model

In domain-driven design, the Domain Model (domain entities and actors) covers all layers involved in modelling a business domain, including (but not limited to) Service Layer, Business Layer, and Data Access Layer thus ensuring effective communication at all levels of engineering. It is considered an effective tool for software development, especially when domain knowledge is iteratively provided by domain experts (such as Business Analysts, Subject Matter Experts and Product Owners.)