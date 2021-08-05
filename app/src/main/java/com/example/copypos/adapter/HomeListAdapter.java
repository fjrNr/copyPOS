package com.example.copypos.adapter;

public class HomeListAdapter{
//    public static final int ITEM_TYPE_PRODUCT = 0;
//    public static final int ITEM_TYPE_PRINT = 1;
//    public static final int ITEM_TYPE_PHOTOCOPY = 2;
//    public static final int ITEM_TYPE_SERVICE = 3;
//
//    private List<Product> products;
//    private List<PrintService> prints;
//    private List<PhotocopyService> photocopies;
//    private List<Service> services;
//    private Context context;
//    private List<Object> items;
//    private ItemClickListener itemClickListener;
//
//    public HomeListAdapter(Context context, List<Object> items, ItemClickListener itemClickListener){
//        this.context = context;
//        this.items = items;
//        this.itemClickListener = itemClickListener;
//    }
//
//    public interface ItemClickListener {
//        void onItemClick(View view, int i);
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view;
//
//        if (viewType == ITEM_TYPE_PRODUCT) {
//            view = layoutInflater.inflate(R.layout.item_product, parent, false);
//            return new ProductViewHolder(view);
//        } else if(viewType == ITEM_TYPE_PRINT){
//            view = layoutInflater.inflate(R.layout.item_service, parent, false);
//            return new PrintServiceViewHolder(view);
//        }else if(viewType == ITEM_TYPE_PHOTOCOPY){
//            view = layoutInflater.inflate(R.layout.item_service, parent, false);
//            return new PhotocopyServiceViewHolder(view);
//        }else {
//            view = layoutInflater.inflate(R.layout.item_service, parent, false);
//            return new ServiceViewHolder(view);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Object item = items.get(position);
//
//        if (holder instanceof ProductViewHolder) {
//            ((ProductViewHolder) holder).bind((Product) item);
//        } else if(holder instanceof PrintServiceViewHolder) {
//            ((PrintServiceViewHolder) holder).bind((PrintService) item);
//        }else if(holder instanceof PhotocopyServiceViewHolder) {
//            ((PhotocopyServiceViewHolder) holder).bind((PhotocopyService) item);
//        }else{
//            ((ServiceViewHolder) holder).bind((Service) item);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return this.items.size();
//    }
//
//    public int getItemViewType(int position) {
//        if (items.get(position) instanceof Product) {
//            return ITEM_TYPE_PRODUCT;
//        } else if (items.get(position) instanceof PrintService) {
//            return ITEM_TYPE_PRINT;
//        }else if (items.get(position) instanceof PhotocopyService) {
//            return ITEM_TYPE_PHOTOCOPY;
//        }else{
//            return ITEM_TYPE_SERVICE;
//        }
//    }
//
//    static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        View item;
//        ImageView imgView;
//        ItemClickListener itemClickListener;
//        ItemClickListener itemClickListener2;
//        TextView tv_name;
//        TextView tv_sellPrice;
//
//        public ProductViewHolder(View itemView) {
//            super(itemView);
//
//            // prepare your ViewHolder
//        }
//
//        public void bind(Product product) {
//            // display your object
//        }
//
//        @Override
//        public void onClick(View view) {
//
//        }
//    }
//
//    static class PrintServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        public PrintServiceViewHolder(View itemView) {
//            super(itemView);
//
//            // prepare your ViewHolder
//        }
//
//        public void bind(PrintService service) {
//            // display your object
//        }
//
//        @Override
//        public void onClick(View view) {
//
//        }
//    }
//
//    static class PhotocopyServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        View item;
//        ItemClickListener itemClickListener;
//        ItemClickListener itemClickListener2;
//        TextView tv_name;
//        TextView tv_sellPrice;
//
//        public PhotocopyServiceViewHolder(View itemView, ItemClickListener itemClickListener) {
//            super(itemView);
//            this.tv_name = (TextView) itemView.findViewById(R.id.name);
//            this.tv_sellPrice = (TextView) itemView.findViewById(R.id.sellPrice);
//            this.item = itemView.findViewById(R.id.list_item);
//            itemView.findViewById(R.id.btn_delete).setVisibility(View.GONE);
//            this.itemClickListener = itemClickListener;
//            this.item.setOnClickListener(this);
//            // prepare your ViewHolder
//        }
//
//        public void bind(PhotocopyService photocopy, int position) {
//            // display your object
//            PhotocopyService service = this.photocopy.get(position);
//            holder.tv_name.setText(service.getName());
//            holder.tv_sellPrice.setText(this.rpFormat.format((long) service.getSellPrice()));
//        }
//
//        @Override
//        public void onClick(View view) {
//
//        }
//    }
//
//    static class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        public ServiceViewHolder(View itemView) {
//            super(itemView);
//
//            // prepare your ViewHolder
//        }
//
//        public void bind(Service service) {
//            // display your object
//        }
//
//        @Override
//        public void onClick(View view) {
//
//        }
//    }

}
